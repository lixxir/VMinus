package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VMinusBlockTagGenerator extends BlockTagsProvider {
    public VMinusBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VMinusMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
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
    }
}