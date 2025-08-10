package net.lixir.vminus.vision;

import net.lixir.vminus.vision.resource.codec.VisionCodecs;
import net.lixir.vminus.vision.util.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class VisionProperties {
    public static void init() { // Need to force load these classes or else the properties will not get registered.
        Class<?>[] classes = {
                Items.class,
                Effects.class,
                Tabs.class,
                Entities.class,
                Blocks.class
        };
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load VisionPropertyTypes nested class: " + clazz.getName(), e);
            }
        }
    }

    private static final Map<VisionType<?>, List<VisionProperty<?>>> REGISTRY = new HashMap<>();
    private static final Map<Key, VisionProperty<?>> PROPERTY_BY_ID = new HashMap<>();

    @Contract("_, _ -> param2")
    public static <T, V> @NotNull VisionProperty<V> register(@NotNull VisionType<T> visionType, VisionProperty<V> property) {
        REGISTRY.computeIfAbsent(visionType, c -> new ArrayList<>()).add(property);
        PROPERTY_BY_ID.put(new Key(visionType, property.getId()), property);
        return property;
    }

    @SuppressWarnings("unchecked")
    public static <T> VisionProperty<T> get(VisionType<T> visionType, String id) {
        return (VisionProperty<T>) PROPERTY_BY_ID.get(new Key(visionType, id));
    }

    @SuppressWarnings("unchecked")
    public static @UnmodifiableView @NotNull List<VisionProperty<?>> fromVisionType(VisionType<?> visionType) {
        List<?> list = REGISTRY.getOrDefault(visionType, List.of());
        return (List<VisionProperty<?>>) Collections.unmodifiableList(list);
    }

    public static class Items {
        public static final VisionProperty<VisionFoodProperties> FOOD = itemProperty(VisionProperty.create("food", VisionCodecs.FOOD_PROPERTIES));
        public static final VisionProperty<SoundEvent> COLLECT_SOUND = itemProperty(VisionProperty.create("collect_sound", VisionCodecs.SOUND_EVENT));
        public static final VisionProperty<UseAnim> USE_ANIMATION = itemProperty(VisionProperty.create("use_animation", VisionCodecs.USE_ANIMATION));
        public static final VisionProperty<Integer> MAX_DAMAGE = itemProperty(VisionProperty.create("max_damage", VisionCodecs.INTEGER));
        public static final VisionProperty<Integer> MAX_STACK_SIZE = itemProperty(VisionProperty.create("max_stack_size", VisionCodecs.INTEGER));
        public static final VisionProperty<Integer> USE_TICKS = itemProperty(VisionProperty.create("use_ticks", VisionCodecs.INTEGER));
        public static final VisionProperty<Integer> MAX_USE_TICKS = itemProperty(VisionProperty.create("max_use_ticks", VisionCodecs.INTEGER));
        public static final VisionProperty<Integer> FUEL_TICKS = itemProperty(VisionProperty.create("fuel_ticks", VisionCodecs.INTEGER));
        public static final VisionProperty<Integer> ENCHANTABILITY = itemProperty(VisionProperty.create("enchantability", VisionCodecs.INTEGER));
        public static final VisionProperty<Boolean> GLINT = itemProperty(VisionProperty.create("glint", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> ENCHANTABLE = itemProperty(VisionProperty.create("enchantable", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> FIRE_RESISTANT = itemProperty(VisionProperty.create("fire_resistant", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> CAN_EQUIP = itemProperty(VisionProperty.create("can_equip", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> BAN = itemProperty(VisionProperty.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionProperty<ItemStackWrapper> USE_REMAINDER = itemProperty(VisionProperty.create("use_remainder", VisionCodecs.ITEM_STACK));
        public static final VisionProperty<ItemReplacement> REPLACE = itemProperty(VisionProperty.create("replace", VisionCodecs.ITEM_REPLACEMENT));
        public static final VisionProperty<EquipmentSlot> EQUIP_SLOT = itemProperty(VisionProperty.create("equip_slot", VisionCodecs.EQUIP_SLOT));
        public static final VisionProperty<Rarity> RARITY = itemProperty(VisionProperty.create("rarity", VisionCodecs.RARITY));
        public static final VisionProperty<VisionAttribute> ATTRIBUTE = itemProperty(VisionProperty.create("attribute", VisionCodecs.ATTRIBUTE));
    }

    public static class Effects {
        public static final VisionProperty<MobEffectCategory> CATEGORY = effectProperty(VisionProperty.create("category", VisionCodecs.EFFECT_CATEGORY));
        public static final VisionProperty<Integer> COLOR = effectProperty(VisionProperty.create("color", VisionCodecs.HEX));
        public static final VisionProperty<Boolean> BAN = effectProperty(VisionProperty.create("ban", VisionCodecs.BOOLEAN));
    }

    public static class Tabs {
        public static final VisionProperty<ItemStackWrapper> ICON = tabProperty(VisionProperty.create("icon", VisionCodecs.ITEM_STACK));
        public static final VisionProperty<Boolean> HIDE = tabProperty(VisionProperty.create("hide", VisionCodecs.BOOLEAN));
        public static final VisionProperty<CreativeOrder> ORDER = tabProperty(VisionProperty.create("order", VisionCodecs.CREATIVE_ORDER));
        public static final VisionProperty<ItemReplacement> REMOVE = tabProperty(VisionProperty.create("remove", VisionCodecs.ITEM_REPLACEMENT));
    }

    public static class Entities {
        public static final VisionProperty<VisionEntityVariant> VARIANT = entityProperty(VisionProperty.create("variant", VisionCodecs.ENTITY_VARIANT));
        public static final VisionProperty<Boolean> BAN = entityProperty(VisionProperty.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> SILENT = entityProperty(VisionProperty.create("silent", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> DAMPENS_VIBRATION = entityProperty(VisionProperty.create("dampens_vibration", VisionCodecs.BOOLEAN));
        public static final VisionProperty<BaseAttribute> BASE_ATTRIBUTE = entityProperty(VisionProperty.create("base_attribute", VisionCodecs.BASE_ATTRIBUTE, false));
    }

    public static class Blocks {
        public static final VisionProperty<Float> SPEED_FACTOR = blockProperty(VisionProperty.create("speed_factor", VisionCodecs.FLOAT));
        public static final VisionProperty<Float> JUMP_FACTOR = blockProperty(VisionProperty.create("jump_factor", VisionCodecs.FLOAT));
        public static final VisionProperty<Float> BLAST_RESISTANCE = blockProperty(VisionProperty.create("blast_resistance", VisionCodecs.FLOAT));
        public static final VisionProperty<Float> HARDNESS = blockProperty(VisionProperty.create("hardness", VisionCodecs.FLOAT));
        public static final VisionProperty<Float> FRICTION = blockProperty(VisionProperty.create("friction", VisionCodecs.FLOAT));
        public static final VisionProperty<Boolean> BAN = blockProperty(VisionProperty.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Block> REPLACE = blockProperty(VisionProperty.create("replace", VisionCodecs.BLOCK));
        public static final VisionProperty<Boolean> EMISSIVE = blockProperty(VisionProperty.create("emissive", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> OCCLUDE = blockProperty(VisionProperty.create("occlude", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Boolean> REDSTONE_CONDUCTOR = blockProperty(VisionProperty.create("redstone_conductor", VisionCodecs.BOOLEAN));
        public static final VisionProperty<Integer> LIGHT_LEVEL = blockProperty(VisionProperty.create("light_level", VisionCodecs.INTEGER));
        public static final VisionProperty<SoundType> SOUND = blockProperty(VisionProperty.create("sound", VisionCodecs.SOUND_TYPE));
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionProperty<V> itemProperty(VisionProperty<V> property) {
        return register(VisionTypes.ITEM, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionProperty<V> blockProperty(VisionProperty<V> property) {
        return register(VisionTypes.BLOCK, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionProperty<V> entityProperty(VisionProperty<V> property) {
        return register(VisionTypes.ENTITY, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionProperty<V> effectProperty(VisionProperty<V> property) {
        return register(VisionTypes.EFFECT, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionProperty<V> tabProperty(VisionProperty<V> property) {
        return register(VisionTypes.TAB, property);
    }

    private record Key(VisionType<?> type, String id) {}
}
