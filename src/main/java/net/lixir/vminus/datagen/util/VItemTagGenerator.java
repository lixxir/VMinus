package net.lixir.vminus.datagen.util;

import net.lixir.vminus.datagen.util.simple.BlockItemDatagen;
import net.lixir.vminus.datagen.util.simple.DatagenObject;
import net.lixir.vminus.datagen.util.simple.DatagenRegistry;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class VItemTagGenerator extends ItemTagsProvider {
    public VItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper, String modId) {
        super(p_275343_, p_275729_, p_275322_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSets);
        simpleDatagen();
    }

    private void simpleDatagen() {
        var ores = tag(Tags.Items.ORES);
        var smallFlowers = tag(ItemTags.SMALL_FLOWERS);
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasTag())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemSimpleDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemSimpleDatagen.getBlockItemRegistryPair();
                Item item = blockItemPair.item();
                switch (simpleDatagen.getType()) {
                    case ORE -> ores.add(item);
                    case FLOWER -> smallFlowers.add(item);
                }
            }
        }
    }

    private void blockSets(BlockSet blockSet) {
        var planks = tag(ItemTags.PLANKS);
        var logs = tag(ItemTags.LOGS);
        var stairs = tag(ItemTags.STAIRS);
        var woodenStairs = tag(ItemTags.WOODEN_STAIRS);
        var slabs = tag(ItemTags.SLABS);
        var woodenSlabs = tag(ItemTags.WOODEN_SLABS);
        var walls = tag(ItemTags.WALLS);
        var fences = tag(ItemTags.FENCES);
        var woodenFences = tag(ItemTags.WOODEN_FENCES);
        var fenceGates = tag(ItemTags.FENCE_GATES);
        var woodenPressurePlates = tag(ItemTags.WOODEN_PRESSURE_PLATES);
        var buttons = tag(ItemTags.BUTTONS);
        var woodenButtons = tag(ItemTags.WOODEN_BUTTONS);
        var doors = tag(ItemTags.DOORS);
        var woodenDoors = tag(ItemTags.WOODEN_DOORS);
        var trapdoors = tag(ItemTags.TRAPDOORS);
        var woodenTrapdoors = tag(ItemTags.WOODEN_TRAPDOORS);

        if (blockSet.isWoodSet()) {
            TagKey<Item> logsTag = blockSet.getLogsTag().getSecond();
            if (logsTag != null) {
                var blockSetLogs = tag(logsTag);
                blockSetLogs.add(blockSet.getLog().item());
                blockSetLogs.add(blockSet.getStrippedLog().item());
                blockSetLogs.add(blockSet.getStrippedWood().item());
                blockSetLogs.add(blockSet.getWood().item());
                logs.addTag(logsTag);
            }

            planks.add(blockSet.getBaseBlock().asItem());
            if (blockSet.getStairs() != null)
                woodenStairs.add(blockSet.getStairs().item());
            if (blockSet.getSlab() != null)
                woodenSlabs.add(blockSet.getSlab().item());
            if (blockSet.getFence() != null)
                woodenFences.add(blockSet.getFence().item());
            if (blockSet.getPressurePlate() != null)
                woodenPressurePlates.add(blockSet.getPressurePlate().item());
            if (blockSet.getPressurePlate() != null)
                woodenButtons.add(blockSet.getPressurePlate().item());
            if (blockSet.getDoor() != null)
                woodenDoors.add(blockSet.getDoor().item());
            if (blockSet.getTrapdoor() != null)
                woodenTrapdoors.add(blockSet.getTrapdoor().item());
        } else {
            if (blockSet.getStairs() != null)
                stairs.add(blockSet.getStairs().item());
            if (blockSet.getSlab() != null)
                slabs.add(blockSet.getSlab().item());
            if (blockSet.getFence() != null)
                fences.add(blockSet.getFence().item());
            if (blockSet.getButton() != null)
                buttons.add(blockSet.getButton().item());
            if (blockSet.getDoor() != null)
                doors.add(blockSet.getDoor().item());
            if (blockSet.getTrapdoor() != null)
                trapdoors.add(blockSet.getTrapdoor().item());
        }
        if (blockSet.getWall() != null)
            walls.add(blockSet.getWall().item());
        if (blockSet.getFenceGate() != null)
            fenceGates.add(blockSet.getFenceGate().item());

    }
}
