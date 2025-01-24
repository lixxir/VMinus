package net.lixir.vminus.datagen.blockset.loottable;

import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockSetLootTables extends BlockLootSubProvider {
    public BlockSetLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            for (RegistryObject<Block> registryBlock : blockSet.getBlocks()) {
                Block block = registryBlock.get();
                if (block instanceof SlabBlock) {
                    this.add(block,
                            slabBlock -> createSlabItemTable(block));
                } else if (block instanceof DoorBlock) {
                    this.add(block,
                            doorBlock -> createDoorTable(block));
                } else {
                    this.dropSelf(block);
                }

            }
        }
    }


    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockSet.BLOCK_SETS.stream()
                .flatMap(blockSet -> blockSet.getBlocks().stream())
                .map(RegistryObject::get)
                .collect(Collectors.toList());
    }
}