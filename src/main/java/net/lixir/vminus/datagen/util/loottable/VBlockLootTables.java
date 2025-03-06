package net.lixir.vminus.datagen.util.loottable;

import net.lixir.vminus.block.ModHangingSignBlock;
import net.lixir.vminus.block.ModStandingSignBlock;
import net.lixir.vminus.block.ModWallHangingSignBlock;
import net.lixir.vminus.block.ModWallSignBlock;
import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class VBlockLootTables extends BlockLootSubProvider {
    private final String modId;

    public VBlockLootTables(String modId) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        this.modId = modId;
    }

    @Override
    protected void generate() {
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSetStates);
    }

    private void blockSetStates(BlockSet blockSet) {
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

        for (BlockSetup blockSetup : SetupRegistries.BLOCKS.getValues(modId)) {
            BlockItemRegistryPair blockItemPair = blockSetup.getBlockItemPair();
            Block block = blockItemPair.block();
            Block baseBlock = blockSetup.getBaseBlock();
            if (baseBlock != null) {
                switch (blockSetup.getDatagenLootTable()) {
                    case DROP_SELF -> this.dropSelf(block);
                }
            }

        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .flatMap(blockSet -> blockSet.getBlockItemPairs().stream())
                .map(BlockItemRegistryPair::block)
                .collect(Collectors.toList());
    }

}