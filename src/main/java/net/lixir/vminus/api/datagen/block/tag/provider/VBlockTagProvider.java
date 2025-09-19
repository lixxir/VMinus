package net.lixir.vminus.api.datagen.block.tag.provider;

import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public abstract class VBlockTagProvider extends BlockTagsProvider {
    public VBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (Block block : VRegistry.fromId(modId).getBlocks()) {
            BlockDefinitionDuck accessor = (BlockDefinitionDuck) block;
            BlockDefinition blockDefinition = accessor.vMinus$getDefinition();
            if (blockDefinition == null)
                continue;
            for (TagKey<Block> tagKey : blockDefinition.getTags()) {
                tag(tagKey).add(block);
            }
        }
    }
}