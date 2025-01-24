package net.lixir.vminus.datagen.blockset;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BlockSetBlockTagGenerator extends BlockTagsProvider {
    public BlockSetBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper, String modId) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        var planks = tag(BlockTags.PLANKS);
        var logs_that_burn = tag(BlockTags.LOGS_THAT_BURN);
        var leaves = tag(BlockTags.LEAVES);

        // Blocksets
        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            assignToolTypeTags(blockSet);
            if (blockSet.isWoodSet()) {
                Block block = blockSet.getBaseBlock();

                planks.add(block);
                if (blockSet.hasLog()) {
                    Block log = blockSet.getLogBlock();
                    Block wood = blockSet.getWoodBlock();
                    Block stripped_log = blockSet.getStrippedLogBlock();
                    Block stripped_wood = blockSet.getStrippedWoodBlock();

                    logs_that_burn.add(log);
                    logs_that_burn.add(wood);
                    logs_that_burn.add(stripped_log);
                    logs_that_burn.add(stripped_wood);
                }
                if (blockSet.hasLeaves()) {
                    Block leavesBlock = blockSet.getLeavesBlock();
                    leaves.add(leavesBlock);
                }
            }
        }
    }

    private void assignToolTypeTags(BlockSet blockSet) {
        for (RegistryObject<Block> BlockRegistry : blockSet.getBlocks()) {
            Block block = BlockRegistry.get();
            switch (blockSet.getTooltype()) {
                case PICKAXE -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                case AXE -> this.tag(BlockTags.MINEABLE_WITH_AXE).add(block);
                case SHOVEL -> this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
                case HOE -> this.tag(BlockTags.MINEABLE_WITH_HOE).add(block);
                default -> VMinus.LOGGER.warn("No valid tool type found for BlockSet: {}", blockSet.getBaseName());
            }
            if (blockSet.getToolStrength() != BlockSet.ToolStrength.NONE) {
                switch (blockSet.getToolStrength()) {
                    case WOODEN -> this.tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(block);
                    case STONE -> this.tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    case IRON -> this.tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    case DIAMOND -> this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block);
                    case NETHERITE -> this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(block);
                }
            }
        }
    }
}