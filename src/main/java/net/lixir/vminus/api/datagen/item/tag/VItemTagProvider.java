package net.lixir.vminus.api.datagen.item.tag;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class VItemTagProvider extends ItemTagsProvider {
    public VItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper, String modId) {
        super(packOutput, provider, tagLookup, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (Pair<Item, ItemDefinition> entryPair : VRegistry.fromId(modId).getItemEntryPairs()) {
            Item item = entryPair.getFirst();
            ItemDefinition itemEntry = entryPair.getSecond();
            for (TagKey<Item> tagKey : itemEntry.getTags()) {
                var tag = tag(tagKey);
                tag.add(item);
            }
        }
    }
}
