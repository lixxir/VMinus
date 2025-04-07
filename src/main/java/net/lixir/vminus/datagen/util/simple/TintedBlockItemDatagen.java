package net.lixir.vminus.datagen.util.simple;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;

public class TintedBlockItemDatagen extends BlockItemDatagen {
    private TintType tintType;

    protected TintedBlockItemDatagen(BlockItemRegistryPair blockItemRegistryPair, Type type, Flags flags, TintType tintType) {
        super(blockItemRegistryPair, type, flags);
        this.tintType = tintType;
    }

    public TintType getTintType() {
        return tintType;
    }

    public TintedBlockItemDatagen withTintType(TintType tintType) {
        this.tintType = tintType;
        return this;
    }

    public enum TintType {
        NONE,
        FOLIAGE,
        GRASS
    }
}
