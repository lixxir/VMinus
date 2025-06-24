package net.lixir.vminus.vision;

import net.lixir.vminus.vision.resource.VisionCodecs;
import net.lixir.vminus.vision.util.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class VisionPropertyTypes {
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

    private static final Map<VisionType<?>, List<VisionPropertyType<?>>> REGISTRY = new HashMap<>();
    private static final Map<String, VisionPropertyType<?>> PROPERTY_BY_ID = new HashMap<>();


    @Contract("_, _ -> param2")
    public static <T, V> @NotNull VisionPropertyType<V> register(@NotNull VisionType<T> visionType, VisionPropertyType<V> property) {
        REGISTRY.computeIfAbsent(visionType, c -> new ArrayList<>()).add(property);
        PROPERTY_BY_ID.put(property.getId(), property);
        return property;
    }

    @SuppressWarnings("unchecked")
    public static <T> VisionPropertyType<T> fromId(String id) {
        return (VisionPropertyType<T>) PROPERTY_BY_ID.get(id);
    }

    @SuppressWarnings("unchecked")
    public static @UnmodifiableView @NotNull List<VisionPropertyType<?>> fromVisionType(VisionType<?> visionType) {
        List<?> list = REGISTRY.getOrDefault(visionType, List.of());
        return (List<VisionPropertyType<?>>) Collections.unmodifiableList(list);
    }

    public static class Items {
        public static final VisionPropertyType<VisionFoodProperties> FOOD = itemProperty(VisionPropertyType.create("food", VisionCodecs.FOOD_PROPERTIES));
        public static final VisionPropertyType<SoundEvent> COLLECT_SOUND = itemProperty(VisionPropertyType.create("collect_sound", VisionCodecs.SOUND_EVENT));
        public static final VisionPropertyType<UseAnim> USE_ANIMATION = itemProperty(VisionPropertyType.create("use_animation", VisionCodecs.USE_ANIMATION));
        public static final VisionPropertyType<Integer> MAX_DAMAGE = itemProperty(VisionPropertyType.create("max_damage", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> MAX_STACK_SIZE = itemProperty(VisionPropertyType.create("max_stack_size", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> USE_TICKS = itemProperty(VisionPropertyType.create("use_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> MAX_USE_TICKS = itemProperty(VisionPropertyType.create("max_use_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> FUEL_TICKS = itemProperty(VisionPropertyType.create("fuel_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> ENCHANTABILITY = itemProperty(VisionPropertyType.create("enchantability", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Boolean> GLINT = itemProperty(VisionPropertyType.create("glint", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> ENCHANTABLE = itemProperty(VisionPropertyType.create("enchantable", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> FIRE_RESISTANT = itemProperty(VisionPropertyType.create("fire_resistant", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> CAN_EQUIP = itemProperty(VisionPropertyType.create("can_equip", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> BAN = itemProperty(VisionPropertyType.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<VisionItemReplacement> REPLACE = itemProperty(VisionPropertyType.create("replace", VisionCodecs.ITEM_REPLACEMENT));
        public static final VisionPropertyType<EquipmentSlot> EQUIP_SLOT = itemProperty(VisionPropertyType.create("equip_slot", VisionCodecs.EQUIP_SLOT));
        public static final VisionPropertyType<Rarity> RARITY = itemProperty(VisionPropertyType.create("rarity", VisionCodecs.RARITY));
        public static final VisionPropertyType<VisionAttribute> ATTRIBUTE = itemProperty(VisionPropertyType.create("attribute", VisionCodecs.ATTRIBUTE));
    }

    public static class Effects {
        public static final VisionPropertyType<MobEffectCategory> CATEGORY = effectProperty(VisionPropertyType.create("category", VisionCodecs.EFFECT_CATEGORY));
        public static final VisionPropertyType<Integer> COLOR = effectProperty(VisionPropertyType.create("color", VisionCodecs.HEX));
        public static final VisionPropertyType<Boolean> BAN = effectProperty(VisionPropertyType.create("ban", VisionCodecs.BOOLEAN));
    }

    public static class Tabs {
        public static final VisionPropertyType<ItemStack> ICON = tabProperty(VisionPropertyType.create("icon", VisionCodecs.ITEM_STACK));
        public static final VisionPropertyType<Boolean> HIDE = tabProperty(VisionPropertyType.create("hide", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<VisionCreativeOrder> ORDER = tabProperty(VisionPropertyType.create("order", VisionCodecs.CREATIVE_ORDER));
        public static final VisionPropertyType<VisionItemReplacement> REMOVE = tabProperty(VisionPropertyType.create("remove", VisionCodecs.ITEM_REPLACEMENT));
    }

    public static class Entities {
        public static final VisionPropertyType<Boolean> BAN = entityProperty(VisionPropertyType.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> SILENT = entityProperty(VisionPropertyType.create("silent", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> DAMPENS_VIBRATION = entityProperty(VisionPropertyType.create("dampens_vibration", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<VisionBaseAttribute> BASE_ATTRIBUTE = entityProperty(VisionPropertyType.create("base_attribute", VisionCodecs.BASE_ATTRIBUTE, false));
    }

    public static class Blocks {
        public static final VisionPropertyType<Float> SPEED_FACTOR = blockProperty(VisionPropertyType.create("speed_factor", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> JUMP_FACTOR = blockProperty(VisionPropertyType.create("jump_factor", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> BLAST_RESISTANCE = blockProperty(VisionPropertyType.create("blast_resistance", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> HARDNESS = blockProperty(VisionPropertyType.create("hardness", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> FRICTION = blockProperty(VisionPropertyType.create("friction", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Boolean> BAN = blockProperty(VisionPropertyType.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Block> REPLACE = blockProperty(VisionPropertyType.create("replace", VisionCodecs.BLOCK));
        public static final VisionPropertyType<Boolean> EMISSIVE = blockProperty(VisionPropertyType.create("emissive", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> OCCLUDE = blockProperty(VisionPropertyType.create("occlude", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> REDSTONE_CONDUCTOR = blockProperty(VisionPropertyType.create("redstone_conductor", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Integer> LIGHT_LEVEL = blockProperty(VisionPropertyType.create("light_level", VisionCodecs.INTEGER));
        public static final VisionPropertyType<SoundType> SOUND = blockProperty(VisionPropertyType.create("sound", VisionCodecs.SOUND_TYPE));
    }



    @Contract("_ -> param1")
    public static <V> @NotNull VisionPropertyType<V> itemProperty(VisionPropertyType<V> property) {
        return register(VisionTypes.ITEM, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionPropertyType<V> blockProperty(VisionPropertyType<V> property) {
        return register(VisionTypes.BLOCK, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionPropertyType<V> entityProperty(VisionPropertyType<V> property) {
        return register(VisionTypes.ENTITY, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionPropertyType<V> effectProperty(VisionPropertyType<V> property) {
        return register(VisionTypes.EFFECT, property);
    }

    @Contract("_ -> param1")
    public static <V> @NotNull VisionPropertyType<V> tabProperty(VisionPropertyType<V> property) {
        return register(VisionTypes.TAB, property);
    }
}
