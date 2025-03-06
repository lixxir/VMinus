package net.lixir.vminus.registry.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockItemRegistryPair extends RegistryPair<Block, Item> {
    public BlockItemRegistryPair(RegistryObject<Block> firstRegistryObject, RegistryObject<Item> secondRegistryObject) {
        super(firstRegistryObject, secondRegistryObject);
    }

    public Item item() {
        return secondRegistryObject != null ? secondRegistryObject.get() : null;
    }

    public RegistryObject<Item> itemObject() {
        return secondRegistryObject;
    }

    public Block block() {
        return firstRegistryObject != null ? firstRegistryObject.get() : null;
    }

    public RegistryObject<Block> blockObject() {
        return firstRegistryObject;
    }

    public static BlockItemRegistryPair create(DeferredRegister<Block> blocks, DeferredRegister<Item> items, String name, Supplier<? extends Block> blockSupplier) {
        RegistryObject<Block> block = blocks.register(name, blockSupplier);
        RegistryObject<Item> item = RegistryUtil.itemForBlock(block, items);
        return new BlockItemRegistryPair(block, item);
    }

    public static BlockItemRegistryPair createDoubleBlock(DeferredRegister<Block> blocks, DeferredRegister<Item> items, String name, Supplier<? extends Block> blockSupplier) {
        RegistryObject<Block> block = blocks.register(name, blockSupplier);
        RegistryObject<Item> item = RegistryUtil.itemForDoubleBlock(block, items);
        return new BlockItemRegistryPair(block, item);
    }

    public static BlockItemRegistryPair create(DeferredRegister<Block> blocks, DeferredRegister<Item> items, String name, Supplier<? extends Block> blockSupplier, Supplier<? extends Item> itemSupplier) {
        RegistryObject<Block> block = blocks.register(name, blockSupplier);
        RegistryObject<Item> item = items.register(name, itemSupplier);
        return new BlockItemRegistryPair(block, item);
    }
}
