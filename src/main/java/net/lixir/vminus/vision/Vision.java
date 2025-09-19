package net.lixir.vminus.vision;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lixir.vminus.util.NbtConversionUtil;
import net.lixir.vminus.resources.data.vision.VisionFormatter;
import net.lixir.vminus.resources.data.vision.codec.VisionCodec;
import net.lixir.vminus.resources.data.vision.VisionManager;
import net.lixir.vminus.vision.values.VisionValue;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class Vision {
    public static final Vision EMPTY = new Vision(ImmutableMap.of());
    private static final Set<Vision> UNIQUE_VISION_POOL = new HashSet<>();
    private final ImmutableMap<String, VisionValue<?>[]> values;

    private Vision(ImmutableMap<String, VisionValue<?>[]> valueMap) {
        this.values = valueMap;
    }


    public static @NotNull Vision get(@NotNull VisionType<?> visionType, @Nullable ResourceLocation id) {
        return visionType.getVisions().getOrDefault(id, EMPTY);
    }

    public static @NotNull Vision getOrAddVision(@NotNull VisionType<?> visionType, ResourceLocation id, Vision vision) {
        for (Vision existing : List.copyOf(UNIQUE_VISION_POOL)) {
            if (existing.equals(vision)) {
                visionType.putVision(id, existing);
                return existing;
            }
        }
        UNIQUE_VISION_POOL.add(vision);
        visionType.putVision(id, vision);
        return vision;
    }

    public static @NotNull Vision get(@NotNull ItemStack stack) {
        return Vision.get(stack.getItem());
    }

    public static @NotNull Vision get(@NotNull Entity entity) {
        return Vision.get(entity.getType());
    }

    public static @NotNull Vision get(@NotNull EntityType<?> entityType) {
        return Vision.get((VisionDuck) entityType);
    }

    public static @NotNull Vision get(@NotNull Item item) {
        return Vision.get((VisionDuck) item);
    }

    public static @NotNull Vision get(@NotNull BlockState state) {
        return Vision.get(state.getBlock());
    }

    public static @NotNull Vision get(@NotNull Block block) {
        return Vision.get((VisionDuck) block);
    }

    public static @NotNull Vision get(@NotNull VisionDuck visionDuck) {
        ResourceLocation id = visionDuck.vMinus$getVisionId();
        VisionType<?> visionType = visionDuck.vMinus$getVisionType();
        return Vision.get(visionType, id);
    }

    public static void resetVisions() {
        VisionManager.clearVisionManagers();
        VisionType.resetAllVisionTypes();
        UNIQUE_VISION_POOL.clear();
    }

    public static <T> @NotNull Vision fromEntry(ResourceLocation visionId, @NotNull VisionEntry<T> visionEntry, @NotNull VisionType<?> visionType) {
        ImmutableMap.Builder<String, VisionValue<?>[]> builder = ImmutableMap.builder();
        for (var entry : visionEntry.getValues().entrySet()) {
            String id = entry.getKey();
            List<? extends VisionValue<?>> valuesList = entry.getValue();

            List<VisionValue<?>> filteredValues = new ArrayList<>();
            VisionValue<?> constantValue = null;

            for (VisionValue<?> visionValue : valuesList) {
                filteredValues.add(visionValue);
                if (constantValue != null && visionValue.getPriority() <= constantValue.getPriority())
                    continue;
                if (constantValue == null && visionValue.isConstant())
                    constantValue = visionValue;
                else if (constantValue != null) {
                    if (visionValue.isConstant()) {
                        filteredValues.remove(constantValue);
                        constantValue = visionValue;
                    } else {
                        filteredValues.remove(constantValue);
                        constantValue = null;
                    }
                }
            }

            VisionValue<?>[] visionProperties = new VisionValue<?>[filteredValues.size()];
            for (int i = 0; i < visionProperties.length; i++) {
                visionProperties[i] = filteredValues.get(i);
            }

            builder.put(id, visionProperties);
        }

        Vision newVision = new Vision(builder.build());
        return getOrAddVision(visionType, visionId, newVision);
    }

    public static void decode(ResourceLocation id, @NotNull CompoundTag originalTag, @NotNull VisionType<?> visionType) {
        CompoundTag compoundTag = originalTag.copy();
        JsonObject jsonObject = NbtConversionUtil.compoundToJson(compoundTag);

        jsonObject = VisionFormatter.processJson(visionType.getId(), visionType.getMultiList(), jsonObject);
        List<VisionProperty<?>> propertyTypes = VisionProperties.fromVisionType(visionType);
        VisionEntry<?> visionEntry = new VisionEntry<>();

        for (VisionProperty<?> property : propertyTypes) {
            String propertyId = property.getId();
            if (jsonObject.has(propertyId)) {
                VisionCodec<?> propertyCodec = property.getCodec();
                List<? extends VisionValue<?>> parsedList = propertyCodec.decode(jsonObject, propertyId);
                visionEntry.addValues(propertyId, parsedList);
            }
        }

        fromEntry(id, visionEntry, visionType);
    }
    // ==================== getValue overloads ====================

    // ---- Item ----
    public static <T> @Nullable T getValue(@NotNull Item item, @NotNull VisionProperty<T> visionProperty) {
        return getValue(item, visionProperty, new VisionContext(item), null);
    }

    public static <T> @Nullable T getValue(@NotNull Item item, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(item).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull Item item, @NotNull VisionProperty<T> visionProperty, T fallback) {
        return getValue(item, visionProperty, new VisionContext(item), fallback);
    }

    public static <T> T getValue(@NotNull Item item, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(item).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ---- ItemStack ----
    public static <T> @Nullable T getValue(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty) {
        return getValue(stack, visionProperty, new VisionContext(stack), null);
    }

    public static <T> @Nullable T getValue(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(stack).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty, T fallback) {
        return getValue(stack, visionProperty, new VisionContext(stack), fallback);
    }

    public static <T> T getValue(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(stack).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ---- Block ----
    public static <T> @Nullable T getValue(@NotNull Block block, @NotNull VisionProperty<T> visionProperty) {
        return getValue(block, visionProperty, new VisionContext(block), null);
    }

    public static <T> @Nullable T getValue(@NotNull Block block, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(block).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull Block block, @NotNull VisionProperty<T> visionProperty, T fallback) {
        return getValue(block, visionProperty, new VisionContext(block), fallback);
    }

    public static <T> T getValue(@NotNull Block block, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(block).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ---- BlockState ----
    public static <T> @Nullable T getValue(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty) {
        return getValue(state, visionProperty, new VisionContext(state), null);
    }

    public static <T> @Nullable T getValue(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(state).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty, @NotNull T fallback) {
        return getValue(state, visionProperty, new VisionContext(state), fallback);
    }

    public static <T> T getValue(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(state).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ---- Entity ----
    public static <T> @Nullable T getValue(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty) {
        return getValue(entity, visionProperty, new VisionContext(entity), null);
    }

    public static <T> @Nullable T getValue(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(entity).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty, @NotNull T fallback) {
        return getValue(entity, visionProperty, new VisionContext(entity), fallback);
    }

    public static <T> T getValue(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(entity).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ---- EntityType ----
    public static <T> @Nullable T getValue(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty) {
        return getValue(entityType, visionProperty, new VisionContext(entityType), null);
    }

    public static <T> @Nullable T getValue(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(entityType).getValue(visionProperty.getId(), visionContext, null);
    }

    public static <T> T getValue(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty, @NotNull T fallback) {
        return getValue(entityType, visionProperty, new VisionContext(entityType), fallback);
    }

    public static <T> T getValue(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return Vision.get(entityType).getValue(visionProperty.getId(), visionContext, fallback);
    }

    // ==================== getValues overloads ====================

    // ---- Item ----
    public static <T> @NotNull List<T> getValues(@NotNull Item item, @NotNull VisionProperty<T> visionProperty) {
        return getValues(item, visionProperty, new VisionContext(item));
    }

    public static <T> @NotNull List<T> getValues(@NotNull Item item, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(item).getValues(visionProperty.getId(), visionContext);
    }

    // ---- ItemStack ----
    public static <T> @NotNull List<T> getValues(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty) {
        return getValues(stack, visionProperty, new VisionContext(stack));
    }

    public static <T> @NotNull List<T> getValues(@NotNull ItemStack stack, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(stack).getValues(visionProperty.getId(), visionContext);
    }

    // ---- Block ----
    public static <T> @NotNull List<T> getValues(@NotNull Block block, @NotNull VisionProperty<T> visionProperty) {
        return getValues(block, visionProperty, new VisionContext(block));
    }

    public static <T> @NotNull List<T> getValues(@NotNull Block block, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(block).getValues(visionProperty.getId(), visionContext);
    }

    // ---- BlockState ----
    public static <T> @NotNull List<T> getValues(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty) {
        return getValues(state, visionProperty, new VisionContext(state));
    }

    public static <T> @NotNull List<T> getValues(@NotNull BlockState state, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(state).getValues(visionProperty.getId(), visionContext);
    }

    // ---- Entity ----
    public static <T> @NotNull List<T> getValues(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty) {
        return getValues(entity, visionProperty, new VisionContext(entity));
    }

    public static <T> @NotNull List<T> getValues(@NotNull Entity entity, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(entity).getValues(visionProperty.getId(), visionContext);
    }

    // ---- EntityType ----
    public static <T> @NotNull List<T> getValues(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty) {
        return getValues(entityType, visionProperty, new VisionContext(entityType));
    }

    public static <T> @NotNull List<T> getValues(@NotNull EntityType<?> entityType, @NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return Vision.get(entityType).getValues(visionProperty.getId(), visionContext);
    }

    private static <T> void encodeProperty(@NotNull VisionProperty<T> propertyType, @NotNull VisionValue<?> visionValue, JsonArray jsonArray) {
        if (!propertyType.getCodec().getClassType().isInstance(visionValue.getValue())) {
            throw new IllegalStateException("Type mismatch for VisionProperty: expected " +
                    propertyType.getCodec().getClassType().getName() +
                    " but got " +
                    visionValue.getValue().getClass().getName());
        }

        @SuppressWarnings("unchecked")
        VisionValue<T> casted = (VisionValue<T>) visionValue;

        JsonObject propertyObject = propertyType.getCodec().encode(casted.getValue());
        if (propertyObject != null)
            jsonArray.add(propertyObject);
    }

    public <T> T getValue(String id) {
        return getValue(id, null, null);
    }

    public <T> T getValue(@NotNull Vision vision, String id) {
        return vision.getValue(id, null, null);
    }

    public <T> @Nullable T getValue(@NotNull VisionProperty<T> visionProperty) {
        return getValue(visionProperty.getId(), null, null);
    }

    public <T> List<T> getValues(String id) {
        return getValue(id, null, null);
    }

    public <T> @Nullable T getValue(@NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return getValue(visionProperty.getId(), visionContext, null);
    }

    public <T> @Nullable T getValue(@NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext, T fallback) {
        return getValue(visionProperty.getId(), visionContext, fallback);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T getValue(String id, @Nullable VisionContext visionContext,  T fallback) {
        VisionValue<T>[] visionProperties = (VisionValue<T>[]) values.getOrDefault(id, null);
        if (visionProperties == null)
            return fallback;
        for (VisionValue<T> visionValue : visionProperties) {
            T value = visionValue.getValue();
            if (visionValue.testConditions(visionContext)) {
                return value;
            }
        }

        return fallback;
    }

    public <T> List<T> getValues(@NotNull VisionProperty<T> visionProperty) {
        return getValues(visionProperty.getId(), null);
    }

    public <T> List<T> getValues(@NotNull VisionProperty<T> visionProperty, @Nullable VisionContext visionContext) {
        return getValues(visionProperty.getId(), visionContext);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getValues(String id, @Nullable VisionContext visionContext) {
        VisionValue<T>[] visionProperties = (VisionValue<T>[]) values.getOrDefault(id, null);
        if (visionProperties == null)
            return Collections.emptyList();
        List<T> valueList = new ArrayList<>();
        for (VisionValue<T> visionValue : visionProperties) {
            T value = visionValue.getValue();
            if (visionValue.testConditions(visionContext)) {
                valueList.add(value);
            }
        }

        return valueList;
    }

    public @NotNull <T> CompoundTag encode(VisionType<T> visionType) {
        JsonObject root = new JsonObject();
        for (Map.Entry<String, VisionValue<?>[]> entry : values.entrySet()) {
            String id = entry.getKey();
            VisionValue<?>[] visionProperties = entry.getValue();

            JsonArray jsonArray = new JsonArray();
            VisionProperty<?> property = VisionProperties.get(visionType, id);
            if (property == null)
                throw new RuntimeException("Could not find property type: " + id);
            if (!property.shouldSyncToClient())
                continue;
            for (VisionValue<?> visionValue : visionProperties) {
                encodeProperty(property, visionValue, jsonArray);
            }

            if (!jsonArray.isEmpty()) {
                root.add(id, jsonArray);
            }
        }
        return NbtConversionUtil.jsonToCompound(root);
    }

    public ImmutableMap<String, VisionValue<?>[]> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Vision vision = (Vision) o;
        return values.equals(vision.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vision {").append("\n");

        for (var entry : values.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" = [");

            VisionValue<?>[] visionProperties = entry.getValue();
            for (int i = 0; i < visionProperties.length; i++) {
                sb.append(visionProperties[i]);
                if (i < visionProperties.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append("]").append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
