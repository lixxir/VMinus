package net.lixir.vminus.datagen.util.tag;

import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
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

public class VItemTagGenerator extends ItemTagsProvider {
    public VItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper, String modId) {
        super(packOutput, provider, tagLookup, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        for (Item item : UnifiedRegistry.fromId(modId).getItems()) {
            ItemEntryAccessor accessor = (ItemEntryAccessor) item;
            ItemEntry itemEntry = accessor.vminus$getEntry();
            if (itemEntry == null)
                continue;

            for (TagKey<Item> tagKey : itemEntry.getTags()) {
                var tag = tag(tagKey);
                tag.add(item);
            }
        }
    }
}
