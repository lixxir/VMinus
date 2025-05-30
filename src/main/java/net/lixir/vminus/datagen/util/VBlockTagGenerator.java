package net.lixir.vminus.datagen.util;

import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
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
        for (Block block : UnifiedRegistry.fromId(modId).getBlocks()) {
            BlockEntryAccessor accessor = (BlockEntryAccessor) block;
            BlockEntry blockEntry = accessor.vminus$getEntry();
            if (blockEntry == null)
                continue;

            for (TagKey<Block> tagKey : blockEntry.getTags()) {
                var tag = tag(tagKey);
                tag.add(block);
            }
        }
    }
}