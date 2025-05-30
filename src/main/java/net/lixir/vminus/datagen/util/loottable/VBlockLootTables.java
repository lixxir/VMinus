package net.lixir.vminus.datagen.util.loottable;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class VBlockLootTables extends BlockLootSubProvider {
    private final String modId;

    public VBlockLootTables(String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void generate() {}

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return super.getKnownBlocks();
    }
}