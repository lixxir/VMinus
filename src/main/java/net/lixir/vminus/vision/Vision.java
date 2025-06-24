package net.lixir.vminus.vision;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.util.NbtConversionUtil;
import net.lixir.vminus.vision.resource.codec.VisionCodec;
import net.lixir.vminus.vision.resource.manager.VisionManager;
import net.lixir.vminus.vision.values.VisionProperty;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.util.*;

public class Vision {
    public static final Vision EMPTY = new Vision(ImmutableMap.of());
    private static final Set<Vision> UNIQUE_VISION_POOL = new HashSet<>();
    private final ImmutableMap<String, VisionProperty<?>[]> properties;

    private Vision(ImmutableMap<String, VisionProperty<?>[]> properties) {
        this.properties = properties;
    }

    public static @NotNull Vision getVision(@NotNull VisionType<?> visionType, ResourceLocation id) {
        return visionType.getVisions().getOrDefault(id, EMPTY);
    }

    public static @NotNull Vision getOrAddVision(@NotNull VisionType<?> visionType, ResourceLocation id, Vision vision) {
        for (Vision existing : UNIQUE_VISION_POOL) {
            if (existing.equals(vision)) {
                visionType.putVision(id, existing);
                return existing;
            }
        }

        UNIQUE_VISION_POOL.add(vision);
        visionType.putVision(id, vision);
        return vision;
    }

    public static @NotNull Vision getVision(@NotNull VisionDuck visionDuck) {
        ResourceLocation id = visionDuck.vMinus$getVisionId();
        VisionType<?> visionType = visionDuck.vMinus$getVisionType();
        return Vision.getVision(visionType, id);
    }

    public static void resetVisions() {
        VisionManager.clearVisionManagers();
        VisionType.resetAllVisionTypes();
        UNIQUE_VISION_POOL.clear();
    }

    public static <T> @NotNull Vision fromEntry(ResourceLocation visionId, @NotNull VisionEntry<T> visionEntry, @NotNull VisionType<?> visionType) {
        ImmutableMap.Builder<String, VisionProperty<?>[]> builder = ImmutableMap.builder();
        for (var entry : visionEntry.getValues().entrySet()) {
            String id = entry.getKey();
            List<? extends VisionProperty<?>> valuesList = entry.getValue();

            List<VisionProperty<?>> filteredValues = new ArrayList<>();
            VisionProperty<?> constantValue = null;

            for (VisionProperty<?> visionProperty : valuesList) {
                filteredValues.add(visionProperty);
                if (constantValue != null && visionProperty.getPriority() <= constantValue.getPriority())
                    continue;
                if (constantValue == null && visionProperty.isConstant())
                    constantValue = visionProperty;
                else if (constantValue != null) {
                    if (visionProperty.isConstant()) {
                        filteredValues.remove(constantValue);
                        constantValue = visionProperty;
                    } else {
                        filteredValues.remove(constantValue);
                        constantValue = null;
                    }
                }
            }

            VisionProperty<?>[] visionProperties = new VisionProperty<?>[filteredValues.size()];
            for (int i = 0; i < visionProperties.length; i++) {
                visionProperties[i] = filteredValues.get(i);
            }

            builder.put(id, visionProperties);
        }

        Vision newVision = new Vision(builder.build());
        return getOrAddVision(visionType, visionId, newVision);
    }

    public <T> T getValue(String id) {
        return getValue(id, null);
    }
    public <T> @Nullable T getValue(@NotNull VisionPropertyType<T> visionPropertyType, @Nullable VisionContext visionContext) {
        return getValue(visionPropertyType.getId(), visionContext);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T getValue(String id, @Nullable VisionContext visionContext) {
        VisionProperty<T>[] visionProperties = (VisionProperty<T>[]) properties.getOrDefault(id, null);
        if (visionProperties == null)
            return null;
        for (VisionProperty<T> visionProperty : visionProperties) {
            T value = visionProperty.getValue();
            if (visionProperty.testConditions(visionContext)) {
                return value;
            }
        }

        return null;
    }

    public <T> List<T> getValues(String id) {
        return getValue(id, null);
    }


    public <T> List<T> getValues(@NotNull VisionPropertyType<T> visionPropertyType) {
        return getValues(visionPropertyType.getId(), null);
    }

    public <T> List<T> getValues(@NotNull VisionPropertyType<T> visionPropertyType, @Nullable VisionContext visionContext) {
        return getValues(visionPropertyType.getId(), visionContext);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getValues(String id, @Nullable VisionContext visionContext) {
        VisionProperty<T>[] visionProperties = (VisionProperty<T>[]) properties.getOrDefault(id, null);
        if (visionProperties == null)
            return Collections.emptyList();
        List<T> valueList = new ArrayList<>();
        for (VisionProperty<T> visionProperty : visionProperties) {
            T value = visionProperty.getValue();
            if (visionProperty.testConditions(visionContext)) {
                valueList.add(value);
            }
        }

        return valueList;
    }

    public static @NotNull Vision fromNbt(ResourceLocation id, @NotNull CompoundTag originalTag, @NotNull VisionType<?> visionType) {
        CompoundTag compoundTag = originalTag.copy();
        JsonObject jsonObject = NbtConversionUtil.compoundToJson(compoundTag);
        VMinus.LOGGER.info("Id={}, Parsed JsonObject={}", id, jsonObject);

       // jsonObject = VisionProcessor.processJson(visionType.id(), visionType.multiList(), jsonObject);  Probably not needed
        List<VisionPropertyType<?>> propertyTypes = VisionPropertyTypes.fromVisionType(visionType);
        VisionEntry<?> visionEntry = new VisionEntry<>();

        for (VisionPropertyType<?> property : propertyTypes) {
            String propertyId = property.getId();
            if (jsonObject.has(propertyId)) {
                VisionCodec<?> propertyCodec = property.getCodec();
                List<? extends VisionProperty<?>> parsedList = propertyCodec.decode(jsonObject, propertyId);
                visionEntry.addValues(propertyId, parsedList);
            }
        }

        return fromEntry(id, visionEntry, visionType);
    }

    public @NotNull CompoundTag toNbt() {
        JsonObject root = new JsonObject();
        for (Map.Entry<String, VisionProperty<?>[]> entry : properties.entrySet()) {
            String id = entry.getKey();
            VisionProperty<?>[] visionProperties = entry.getValue();

            JsonArray jsonArray = new JsonArray();
            VisionPropertyType<Object> propertyType = VisionPropertyTypes.fromId(id);
            if (propertyType == null)
                throw new RuntimeException("Could not find property type: " + id);
            if (!propertyType.shouldSyncToClient())
                continue;
            for (VisionProperty<?> visionProperty : visionProperties) {
                encodeProperty(propertyType, visionProperty, jsonArray);
            }

            if (!jsonArray.isEmpty()) {
                root.add(id, jsonArray);
            }
        }
        return NbtConversionUtil.jsonToCompound(root);
    }


    private static <T> void encodeProperty(
            @NotNull VisionPropertyType<T> propertyType,
            @NotNull VisionProperty<?> visionProperty,
            JsonArray jsonArray
    ) {
        Object value = visionProperty.getValue();

        if (propertyType.getCodec().getClassType().isInstance(value)) {
            T castedValue = propertyType.getCodec().getClassType().cast(value);
            JsonObject propertyObject = propertyType.getCodec().encode(castedValue);
            if (propertyObject != null) {
                jsonArray.add(propertyObject);
            }
        }
    }




    public ImmutableMap<String, VisionProperty<?>[]> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Vision vision = (Vision) o;
        return properties.equals(vision.properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vision {").append("\n");

        for (var entry : properties.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" = [");

            VisionProperty<?>[] visionProperties = entry.getValue();
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
