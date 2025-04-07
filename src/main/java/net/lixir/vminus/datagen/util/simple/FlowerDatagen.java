package net.lixir.vminus.datagen.util.simple;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.world.item.Item;

public class FlowerDatagen extends TintedBlockItemDatagen {

    private final Item dyeItem;

    public FlowerDatagen(BlockItemRegistryPair blockItemRegistryPair, Item dyeItem) {
        super(blockItemRegistryPair, Type.FLOWER, new Flags(), TintType.NONE);
        this.dyeItem = dyeItem;
    }

    public FlowerDatagen(BlockItemRegistryPair blockItemRegistryPair, Item dyeItem, Flags flags) {
        super(blockItemRegistryPair, Type.FLOWER, flags, TintType.NONE);
        this.dyeItem = dyeItem;
    }

    public Item getDyeItem() {
        return dyeItem;
    }
}
