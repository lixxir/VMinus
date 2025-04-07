package net.lixir.vminus.datagen.util.simple;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;

public class BlockItemDatagen extends DatagenObject {
    private final BlockItemRegistryPair blockItemRegistryPair;

    protected BlockItemDatagen(BlockItemRegistryPair blockItemRegistryPair, Type type, Flags flags) {
        super(type, flags);
        this.blockItemRegistryPair = blockItemRegistryPair;
    }

    public BlockItemRegistryPair getBlockItemRegistryPair() {
        return blockItemRegistryPair;
    }
}
