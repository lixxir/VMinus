package net.lixir.vminus.vision;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("deprecation")
public class VisionTypes {
    public static void init() {}

    private static final Map<String, VisionType<?>> REGISTRY = new HashMap<>();

    @Contract("_ -> param1")
    public static <T> @NotNull VisionType<T> register(VisionType<T> visionType) {
        REGISTRY.put(visionType.getId(), visionType);
        return visionType;
    }

    public static @Nullable VisionType<?> get(String id){
        return REGISTRY.getOrDefault(id, null);
    }

    public static @NotNull Collection<VisionType<?>> getAll() {
        return REGISTRY.values();
    }

    public static final VisionType<Item> ITEM = register(new VisionType<>(
            "item", "items", BuiltInRegistries.ITEM,
            BuiltInRegistries.ITEM::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));

    public static final VisionType<CreativeModeTab> TAB = register(new VisionType<>(
            "tab", "tabs", BuiltInRegistries.CREATIVE_MODE_TAB,
            BuiltInRegistries.CREATIVE_MODE_TAB::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));

    public static final VisionType<EntityType<?>> ENTITY = register(new VisionType<>(
            "entity", "entities", BuiltInRegistries.ENTITY_TYPE,
            BuiltInRegistries.ENTITY_TYPE::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));

    public static final VisionType<MobEffect> EFFECT = register(new VisionType<>(
            "effect", "effects", BuiltInRegistries.MOB_EFFECT,
            BuiltInRegistries.MOB_EFFECT::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));

    public static final VisionType<Enchantment> ENCHANTMENT = register(new VisionType<>(
            "enchantment", "enchantments", BuiltInRegistries.ENCHANTMENT,
            BuiltInRegistries.ENCHANTMENT::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));

    public static final VisionType<Block> BLOCK = register(new VisionType<>(
            "block", "blocks", BuiltInRegistries.BLOCK,
            BuiltInRegistries.BLOCK::get,
            (obj, id) -> ((VisionDuck) obj).vMinus$setVisionId(id)
    ));



}
