package net.lixir.vminus.datagen.util;

import net.lixir.vminus.datagen.util.simple.BlockItemDatagen;
import net.lixir.vminus.datagen.util.simple.DatagenObject;
import net.lixir.vminus.datagen.util.simple.DatagenRegistry;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class VBlockTagGenerator extends BlockTagsProvider {
    public VBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        // Blocksets
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSets);

        var walls = tag(BlockTags.WALLS);
        var stairs = tag(BlockTags.STAIRS);
        var slabs = tag(BlockTags.SLABS);
        var leaves = tag(BlockTags.LEAVES);
        var flowers = tag(BlockTags.FLOWERS);
        var smallFlowers = tag(BlockTags.SMALL_FLOWERS);
        var swordEfficient = tag(BlockTags.SWORD_EFFICIENT);
        var pickaxeMineable = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var axeMineable = tag(BlockTags.MINEABLE_WITH_AXE);
        var shovelMineable = tag(BlockTags.MINEABLE_WITH_SHOVEL);
        var hoeMineable = tag(BlockTags.MINEABLE_WITH_HOE);
        simpleDatagen();
    }

    private void simpleDatagen() {
        var ores = tag(Tags.Blocks.ORES);
        var smallFlowers = tag(BlockTags.SMALL_FLOWERS);
        var replaceable = tag(BlockTags.REPLACEABLE);
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasTag())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemSimpleDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemSimpleDatagen.getBlockItemRegistryPair();
                Block block = blockItemPair.block();
                switch (simpleDatagen.getType()) {
                    case ORE -> ores.add(block);
                    case FLOWER -> smallFlowers.add(block);
                    case PLANT -> replaceable.add(block);
                }
            }
        }
    }

    private void blockSets(BlockSet blockSet) {
        var planks = tag(BlockTags.PLANKS);
        var logs = tag(BlockTags.LOGS);
        var stairs = tag(BlockTags.STAIRS);
        var woodenStairs = tag(BlockTags.WOODEN_STAIRS);
        var slabs = tag(BlockTags.SLABS);
        var woodenSlabs = tag(BlockTags.WOODEN_SLABS);
        var walls = tag(BlockTags.WALLS);
        var fences = tag(BlockTags.FENCES);
        var woodenFences = tag(BlockTags.WOODEN_FENCES);
        var fenceGates = tag(BlockTags.FENCE_GATES);
        var pressurePlates = tag(BlockTags.PRESSURE_PLATES);
        var woodenPressurePlates = tag(BlockTags.WOODEN_PRESSURE_PLATES);
        var buttons = tag(BlockTags.BUTTONS);
        var woodenButtons = tag(BlockTags.WOODEN_BUTTONS);
        var doors = tag(BlockTags.DOORS);
        var woodenDoors = tag(BlockTags.WOODEN_DOORS);
        var trapdoors = tag(BlockTags.TRAPDOORS);
        var woodenTrapdoors = tag(BlockTags.WOODEN_TRAPDOORS);

        if (blockSet.isWoodSet()) {
            TagKey<Block> logsTag = blockSet.getLogsTag().getFirst();
            if (logsTag != null) {
                var blockSetLogs = tag(logsTag);
                blockSetLogs.add(blockSet.getLog().block());
                blockSetLogs.add(blockSet.getStrippedLog().block());
                blockSetLogs.add(blockSet.getStrippedWood().block());
                blockSetLogs.add(blockSet.getWood().block());
                logs.addTag(logsTag);
            }

            planks.add(blockSet.getBaseBlock());
            if (blockSet.getStairs() != null)
                woodenStairs.add(blockSet.getStairs().block());
            if (blockSet.getSlab() != null)
                woodenSlabs.add(blockSet.getSlab().block());
            if (blockSet.getFence() != null)
                woodenFences.add(blockSet.getFence().block());
            if (blockSet.getPressurePlate() != null)
                woodenPressurePlates.add(blockSet.getPressurePlate().block());
            if (blockSet.getPressurePlate() != null)
                woodenButtons.add(blockSet.getPressurePlate().block());
            if (blockSet.getDoor() != null)
                woodenDoors.add(blockSet.getDoor().block());
            if (blockSet.getTrapdoor() != null)
                woodenTrapdoors.add(blockSet.getTrapdoor().block());
        } else {
            if (blockSet.getStairs() != null)
                stairs.add(blockSet.getStairs().block());
            if (blockSet.getSlab() != null)
                slabs.add(blockSet.getSlab().block());
            if (blockSet.getFence() != null)
                fences.add(blockSet.getFence().block());
            if (blockSet.getPressurePlate() != null)
                pressurePlates.add(blockSet.getPressurePlate().block());
            if (blockSet.getButton() != null)
                buttons.add(blockSet.getButton().block());
            if (blockSet.getDoor() != null)
                doors.add(blockSet.getDoor().block());
            if (blockSet.getTrapdoor() != null)
                trapdoors.add(blockSet.getTrapdoor().block());
        }

        if (blockSet.getWall() != null)
            walls.add(blockSet.getWall().block());
        if (blockSet.getFenceGate() != null)
            fenceGates.add(blockSet.getFenceGate().block());

        for (BlockItemRegistryPair blockItemPair : blockSet.getBlockItemPairs()) {
            Block block = blockItemPair.block();
            for (TagKey<Block> blockTag : blockSet.getBlockTags()) {
                var tag = tag(blockTag);
                tag.add(block);
            }
        }
    }
}