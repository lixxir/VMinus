package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.registry.util.BlockSet;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VMinusBlockTagGenerator extends BlockTagsProvider {
    public VMinusBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VMinusMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        var froglights = tag(VMinusTags.Blocks.FROGLIGHTS);
        froglights.add(Blocks.OCHRE_FROGLIGHT);
        froglights.add(Blocks.VERDANT_FROGLIGHT);
        froglights.add(Blocks.PEARLESCENT_FROGLIGHT);

        var torches = tag(VMinusTags.Blocks.TORCHES);
        torches.add(Blocks.TORCH);
        torches.add(Blocks.WALL_TORCH);

        var soul_torches = tag(VMinusTags.Blocks.SOUL_TORCHES);
        soul_torches.add(Blocks.SOUL_TORCH);
        soul_torches.add(Blocks.SOUL_WALL_TORCH);

        var mob_heads = tag(VMinusTags.Blocks.MOB_HEADS);
        var concrete_powder = tag(VMinusTags.Blocks.CONCRETE_POWDER);

        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            Block block = entry.getValue();
            if (block instanceof ConcretePowderBlock) {
                concrete_powder.add(block);
            } else if (block instanceof SkullBlock || block instanceof WallSkullBlock) {
                mob_heads.add(block);
            }
        }


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
                default -> VMinusMod.LOGGER.warn("No valid tool type found for BlockSet: {}", blockSet.getBaseName());
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