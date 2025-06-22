package net.lixir.vminus.vision;

import net.lixir.vminus.vision.resource.VisionCodecs;
import net.lixir.vminus.vision.util.VisionAttribute;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.*;

public class VisionPropertyTypes {
    private static final Map<Class<?>, List<VisionPropertyType<?>>> REGISTRY = new HashMap<>();
    private static final Map<String, VisionPropertyType<?>> PROPERTY_BY_ID = new HashMap<>();


    public static <T, E> VisionPropertyType<T> register(VisionType visionType, VisionPropertyType<T> property) {
        REGISTRY.computeIfAbsent(visionType.classType(), c -> new ArrayList<>()).add(property);
        PROPERTY_BY_ID.put(property.getId(), property);
        return property;
    }

    @SuppressWarnings("unchecked")
    public static <T> VisionPropertyType<T> fromId(String id) {
        return (VisionPropertyType<T>) PROPERTY_BY_ID.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<VisionPropertyType<?>> fromClass(Class<T> targetType) {
        List<?> list = REGISTRY.getOrDefault(targetType, List.of());
        return (List<VisionPropertyType<?>>) Collections.unmodifiableList(list);
    }

    public static class Items {
        public static final VisionPropertyType<VisionFoodProperties> FOOD = register(VisionTypes.ITEM, VisionPropertyType.create("food", VisionCodecs.FOOD_PROPERTIES));
        public static final VisionPropertyType<UseAnim> USE_ANIMATION = register(VisionTypes.ITEM, VisionPropertyType.create("use_animation", VisionCodecs.USE_ANIMATION));
        public static final VisionPropertyType<Integer> MAX_DAMAGE = register(VisionTypes.ITEM, VisionPropertyType.create("max_damage", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> MAX_STACK_SIZE = register(VisionTypes.ITEM, VisionPropertyType.create("max_stack_size", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> USE_TICKS = register(VisionTypes.ITEM, VisionPropertyType.create("use_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> MAX_USE_TICKS = register(VisionTypes.ITEM, VisionPropertyType.create("max_use_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> FUEL_TICKS = register(VisionTypes.ITEM, VisionPropertyType.create("fuel_ticks", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Integer> ENCHANTABILITY = register(VisionTypes.ITEM, VisionPropertyType.create("enchantability", VisionCodecs.INTEGER));
        public static final VisionPropertyType<Boolean> GLINT = register(VisionTypes.ITEM, VisionPropertyType.create("glint", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> ENCHANTABLE = register(VisionTypes.ITEM, VisionPropertyType.create("enchantable", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> FIRE_RESISTANT = register(VisionTypes.ITEM, VisionPropertyType.create("fire_resistant", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> CAN_EQUIP = register(VisionTypes.ITEM, VisionPropertyType.create("can_equip", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<EquipmentSlot> EQUIP_SLOT = register(VisionTypes.ITEM, VisionPropertyType.create("equip_slot", VisionCodecs.EQUIP_SLOT));
        public static final VisionPropertyType<Rarity> RARITY = register(VisionTypes.ITEM, VisionPropertyType.create("rarity", VisionCodecs.RARITY));
        public static final VisionPropertyType<VisionAttribute> ATTRIBUTE = register(VisionTypes.ITEM, VisionPropertyType.create("attribute", VisionCodecs.ATTRIBUTE));
    }

    public static class Blocks {
        public static final VisionPropertyType<Float> SPEED_FACTOR = register(VisionTypes.BLOCK, VisionPropertyType.create("speed_factor", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> JUMP_FACTOR = register(VisionTypes.BLOCK, VisionPropertyType.create("jump_factor", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> BLAST_RESISTANCE = register(VisionTypes.BLOCK, VisionPropertyType.create("blast_resistance", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> HARDNESS = register(VisionTypes.BLOCK, VisionPropertyType.create("hardness", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Float> FRICTION = register(VisionTypes.BLOCK, VisionPropertyType.create("friction", VisionCodecs.FLOAT));
        public static final VisionPropertyType<Boolean> BAN = register(VisionTypes.BLOCK, VisionPropertyType.create("ban", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Block> REPLACE = register(VisionTypes.BLOCK, VisionPropertyType.create("replace", VisionCodecs.BLOCK));
        public static final VisionPropertyType<Boolean> EMISSIVE = register(VisionTypes.BLOCK, VisionPropertyType.create("emissive", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> OCCLUDE = register(VisionTypes.BLOCK, VisionPropertyType.create("occlude", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Boolean> REDSTONE_CONDUCTOR = register(VisionTypes.BLOCK, VisionPropertyType.create("redstone_conductor", VisionCodecs.BOOLEAN));
        public static final VisionPropertyType<Integer> LIGHT_LEVEL = register(VisionTypes.BLOCK, VisionPropertyType.create("light_level", VisionCodecs.INTEGER));
        public static final VisionPropertyType<SoundType> SOUND = register(VisionTypes.BLOCK, VisionPropertyType.create("sound", VisionCodecs.SOUND_TYPE));
    }
}
