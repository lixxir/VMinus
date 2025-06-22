package net.lixir.vminus.vision;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class VisionTypes {
    public static void init() {}

    private static final Map<String, VisionType> REGISTRY = new HashMap<>();

    @Contract("_ -> param1")
    public static @NotNull VisionType register(VisionType visionType) {
        REGISTRY.put(visionType.id(), visionType);
        return visionType;
    }

    public static @Nullable VisionType get(String id){
        return REGISTRY.getOrDefault(id, null);
    }

    public static @NotNull @UnmodifiableView Collection<VisionType> getAll() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    public static final VisionType ITEM = register(new VisionType(
            "item", "items", "visions/items", Item.class,
            BuiltInRegistries.ITEM::get,
            (obj, index) -> ((VisionDuck) obj).vMinus$setVisionIndex(index)
    ));

    public static final VisionType BLOCK = register(new VisionType(
            "block", "blocks", "visions/blocks", Block.class,
            BuiltInRegistries.BLOCK::get,
            (obj, index) -> ((VisionDuck) obj).vMinus$setVisionIndex(index)
    ));

}
