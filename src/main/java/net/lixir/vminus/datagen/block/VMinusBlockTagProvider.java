package net.lixir.vminus.datagen.block;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.datagen.block.tag.provider.VBlockTagProvider;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VMinusBlockTagProvider extends VBlockTagProvider {
    public VMinusBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VMinus.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        super.addTags(pProvider);
        tag(VMinusTags.Blocks.DEAD_CORAL_BLOCKS)
                .add(Blocks.DEAD_TUBE_CORAL_BLOCK)
                .add(Blocks.DEAD_BRAIN_CORAL_BLOCK)
                .add(Blocks.DEAD_BUBBLE_CORAL_BLOCK)
                .add(Blocks.DEAD_FIRE_CORAL_BLOCK)
                .add(Blocks.DEAD_HORN_CORAL_BLOCK);

        tag(VMinusTags.Blocks.DEAD_CORALS)
                .add(Blocks.DEAD_TUBE_CORAL)
                .add(Blocks.DEAD_BRAIN_CORAL)
                .add(Blocks.DEAD_BUBBLE_CORAL)
                .add(Blocks.DEAD_FIRE_CORAL)
                .add(Blocks.DEAD_HORN_CORAL);

        tag(VMinusTags.Blocks.DEAD_CORAL_FANS)
                .add(Blocks.DEAD_TUBE_CORAL_FAN)
                .add(Blocks.DEAD_BRAIN_CORAL_FAN)
                .add(Blocks.DEAD_BUBBLE_CORAL_FAN)
                .add(Blocks.DEAD_FIRE_CORAL_FAN)
                .add(Blocks.DEAD_HORN_CORAL_FAN);

        tag(VMinusTags.Blocks.WOODEN_FENCE_GATES);
        tag(VMinusTags.Blocks.LEASHABLE)
                .addTag(BlockTags.FENCES);

        tag(VMinusTags.Blocks.COCAO_PLANTABLE_ON)
                .add(Blocks.JUNGLE_LOG);

        tag(VMinusTags.Blocks.FROGLIGHTS)
                .add(Blocks.OCHRE_FROGLIGHT)
                .add(Blocks.VERDANT_FROGLIGHT)
                .add(Blocks.PEARLESCENT_FROGLIGHT);

        tag(VMinusTags.Blocks.ALL_TORCHES)
                .addTag(VMinusTags.Blocks.TORCHES)
                .addTag(VMinusTags.Blocks.SOUL_TORCHES)
                .addTag(VMinusTags.Blocks.REDSTONE_TORCHES);

        tag(VMinusTags.Blocks.TORCHES)
                .add(Blocks.TORCH)
                .add(Blocks.WALL_TORCH);

        tag(VMinusTags.Blocks.REDSTONE_TORCHES)
                .add(Blocks.REDSTONE_TORCH)
                .add(Blocks.REDSTONE_WALL_TORCH);

        var soulTorches = tag(VMinusTags.Blocks.SOUL_TORCHES);
        soulTorches.add(Blocks.SOUL_TORCH);
        soulTorches.add(Blocks.SOUL_WALL_TORCH);

        var mobHeads = tag(VMinusTags.Blocks.MOB_HEADS);
        var dyed = tag(VMinusTags.Blocks.DYED);
        dyed.addTag(VMinusTags.Blocks.DYED_WOOL);
        dyed.addTag(VMinusTags.Blocks.DYED_BEDS);
        dyed.addTag(VMinusTags.Blocks.DYED_BANNERS);
        dyed.addTag(VMinusTags.Blocks.DYED_CANDLES);
        dyed.addTag(VMinusTags.Blocks.DYED_CARPETS);
        dyed.addTag(VMinusTags.Blocks.DYED_CONCRETE);
        dyed.addTag(VMinusTags.Blocks.DYED_CONCRETE_POWDER);
        dyed.addTag(VMinusTags.Blocks.DYED_STAINED_GLASS);
        dyed.addTag(VMinusTags.Blocks.DYED_STAINED_GLASS_PANE);
        dyed.addTag(VMinusTags.Blocks.DYED_TERRACOTTA);
        dyed.addTag(VMinusTags.Blocks.DYED_GLAZED_TERRACOTTA);

        var concretePowder = tag(VMinusTags.Blocks.CONCRETE_POWDER);

        var dyedConcrete = tag(VMinusTags.Blocks.DYED_CONCRETE);
        var dyedConcretePowder = tag(VMinusTags.Blocks.DYED_CONCRETE_POWDER);
        var dyedWool = tag(VMinusTags.Blocks.DYED_WOOL);
        var dyedCarpets = tag(VMinusTags.Blocks.DYED_CARPETS);
        var dyedCandles = tag(VMinusTags.Blocks.DYED_CANDLES);
        var dyedBanners = tag(VMinusTags.Blocks.DYED_BANNERS);
        var dyedBeds = tag(VMinusTags.Blocks.DYED_BEDS);
        var dyedStainedGlass = tag(VMinusTags.Blocks.DYED_STAINED_GLASS);
        var dyedStainedGlassPane = tag(VMinusTags.Blocks.DYED_STAINED_GLASS_PANE);
        var dyedTerracotta = tag(VMinusTags.Blocks.DYED_TERRACOTTA);
        var dyedGlazedTerracotta = tag(VMinusTags.Blocks.DYED_GLAZED_TERRACOTTA);

        var dyeable = tag(VMinusTags.Blocks.DYEABLE);
        dyeable.add(Blocks.WHITE_WOOL);
        dyeable.add(Blocks.CANDLE);
        dyeable.add(Blocks.WHITE_BANNER);
        dyeable.add(Blocks.WHITE_WALL_BANNER);
        dyeable.add(Blocks.WHITE_CONCRETE);
        dyeable.add(Blocks.WHITE_CARPET);
        dyeable.add(Blocks.TERRACOTTA);
        dyeable.add(Blocks.WHITE_GLAZED_TERRACOTTA);
        dyeable.add(Blocks.GLASS);
        dyeable.add(Blocks.GLASS_PANE);
        dyeable.add(Blocks.WHITE_BED);
        dyeable.add(Blocks.WHITE_CONCRETE_POWDER);

        var grasses = tag(VMinusTags.Blocks.GRASSES);
        grasses.add(Blocks.GRASS);

        var tallGrasses = tag(VMinusTags.Blocks.TALL_GRASSES);
        tallGrasses.add(Blocks.TALL_GRASS);

        var brushable = tag(VMinusTags.Blocks.BRUSHABLE);

        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            Block block = entry.getValue();
            String pathName = entry.getKey().location().getPath();
            if (block instanceof ConcretePowderBlock) {
                concretePowder.add(block);
            } else if (block instanceof SkullBlock || block instanceof WallSkullBlock) {
                mobHeads.add(block);
            } else if (block instanceof BrushableBlock) {
                brushable.add(block);
            }
            if (!pathName.startsWith("white_") && pathName.endsWith("_concrete")) {
                dyedConcrete.add(block);
            } else if (!pathName.startsWith("white_") && pathName.endsWith("_wool")) {
                dyedWool.add(block);
            } else if (pathName.endsWith("_candle")) {
                dyedCandles.add(block);
            } else if (pathName.endsWith("_terracotta") && !pathName.endsWith("_glazed_terracotta")) {
                dyedTerracotta.add(block);
            } else if (!pathName.startsWith("white_") && pathName.endsWith("_glazed_terracotta")) {
                dyedGlazedTerracotta.add(block);
            } else if (block instanceof CandleBlock && pathName.endsWith("_candle")) {
                dyedCandles.add(block);
            } else if (!pathName.startsWith("white_") && block instanceof WoolCarpetBlock && pathName.endsWith("_carpet")) {
                dyedCarpets.add(block);
            } else if (block instanceof StainedGlassBlock && pathName.endsWith("_stained_glass") && !pathName.endsWith("_stained_glass_pane")) {
                dyedStainedGlass.add(block);
            }  else if (block instanceof StainedGlassPaneBlock && pathName.endsWith("_stained_glass_pane")) {
                dyedStainedGlassPane.add(block);
            } else if (!pathName.startsWith("white_") && block instanceof BedBlock && pathName.endsWith("_bed")) {
                dyedBeds.add(block);
            } else if (!pathName.startsWith("white_") && block instanceof BannerBlock && pathName.endsWith("_banner") || pathName.endsWith("_wall_banner")) {
                dyedBanners.add(block);
            }else if (!pathName.startsWith("white_") && block instanceof ConcretePowderBlock && pathName.endsWith("_concrete_powder")) {
                dyedConcretePowder.add(block);
            }
        }

        var canSustainPlants = tag(VMinusTags.Blocks.CAN_SUSTAIN_PLANTS);
        var canSustainCactus = tag(VMinusTags.Blocks.CAN_SUSTAIN_CACTUS);
        var canSustainDeadBush = tag(VMinusTags.Blocks.CAN_SUSTAIN_DEAD_BUSH);
    }

}