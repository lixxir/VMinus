package net.lixir.vminus.datagen.util.loottable;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.block.ModHangingSignBlock;
import net.lixir.vminus.block.ModStandingSignBlock;
import net.lixir.vminus.block.ModWallHangingSignBlock;
import net.lixir.vminus.block.ModWallSignBlock;
import net.lixir.vminus.datagen.util.simple.*;
import net.lixir.vminus.datagen.util.simple.BlockItemDatagen;
import net.lixir.vminus.datagen.util.simple.DatagenObject;
import net.lixir.vminus.registry.VMinusBlocks;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    protected void generate() {
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSets);
        simpleDatagen();
    }

    private void simpleDatagen() {
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasLootTable())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemSimpleDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemSimpleDatagen.getBlockItemRegistryPair();
                Block block = blockItemPair.block();
                switch (simpleDatagen.getType()) {
                    case ORE -> this.add(block, createOreDrop(block, ((OreDatagen) blockItemSimpleDatagen).getOreDrop().get()));
                    case PLANT, FLOWER, LARGE_FLOWER, LARGE_PLANT -> this.dropSelf(block);
                }
            }
        }
    }

    private void blockSets(BlockSet blockSet) {
        for (BlockItemRegistryPair blockItemPair : blockSet.getBlockItemPairs()) {
            Block block = blockItemPair.block();
            Item item = blockItemPair.item();
            if (block instanceof SlabBlock) {
                this.add(block, slabBlock -> createSlabItemTable(block));
            } else if (block instanceof DoorBlock) {
                this.add(block, doorBlock -> createDoorTable(block));
            } else if (block instanceof ModStandingSignBlock
                    || block instanceof ModWallSignBlock || block instanceof ModHangingSignBlock || block instanceof ModWallHangingSignBlock) {
                this.add(block, doorBlock -> createSingleItemTable(item));
            } else {
                this.dropSelf(block);
            }
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        ArrayList<Block> knownBlocks = BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .flatMap(blockSet -> blockSet.getBlockItemPairs().stream())
                .map(BlockItemRegistryPair::block).collect(Collectors.toCollection(ArrayList::new));
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasLootTable())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemDatagen) {
                Block block = blockItemDatagen.getBlockItemRegistryPair().block();
                knownBlocks.add(block);
            }
        }
        VMinus.LOGGER.info(knownBlocks);
        return knownBlocks;
    }

}