package net.lixir.vminus.datagen.util.simple;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.world.item.Item;

public class PlantDatagen extends TintedBlockItemDatagen {

    public PlantDatagen(BlockItemRegistryPair blockItemRegistryPair) {
        super(blockItemRegistryPair, Type.PLANT, new Flags(), TintType.NONE);
    }

    public PlantDatagen(BlockItemRegistryPair blockItemRegistryPair, Flags flags) {
        super(blockItemRegistryPair, Type.PLANT, flags, TintType.NONE);
    }

}
