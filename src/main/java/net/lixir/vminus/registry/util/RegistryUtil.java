package net.lixir.vminus.registry.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RegistryUtil {


    public static RegistryObject<Item> itemForBlock(RegistryObject<Block> block, DeferredRegister<Item> items) {
        return items.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static RegistryObject<Item> itemForDoubleBlock(RegistryObject<Block> block, DeferredRegister<Item> items) {
        return items.register(block.getId().getPath(), () -> new DoubleHighBlockItem(block.get(), new Item.Properties()));
    }
}
