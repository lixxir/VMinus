package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.BlockModel;
import net.lixir.vminus.registry.ItemModel;
import net.lixir.vminus.registry.TaggedRegistryEntry;
import net.lixir.vminus.registry.TintType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemEntry extends RegistryEntry<ItemEntry, Item> implements TaggedRegistryEntry<ItemEntry, Item> {
    protected final List<TagKey<Item>> tags = new ArrayList<>();
    protected ItemModel model = ItemModel.UNSET;
    protected TintType tintType;
    private boolean isFromBlock = false;

    private ItemEntry() {}

    @Contract(value = " -> new", pure = true)
    public static @NotNull ItemEntry of() {
        return new ItemEntry();
    }

    public static @NotNull ItemEntry from(Block block) {
        return from(((BlockEntryAccessor) block).vminus$getEntry());
    }

    public static @NotNull ItemEntry from(@Nullable BlockEntry blockEntry) {
        ItemEntry itemEntry = new ItemEntry();
        if (blockEntry != null) {
            itemEntry.langValue = null;
            itemEntry.tintType = blockEntry.tintType;
            BlockModel blockModel = blockEntry.getModel();
            if (blockModel != null)
                itemEntry.model = blockModel.getItemModel();
            itemEntry.isFromBlock = true;
        }
        return itemEntry;
    }



    public static @NotNull ItemEntry copy(Item item) {
        ItemEntryAccessor accessor = (ItemEntryAccessor) item;
        ItemEntry itemEntry = accessor.vminus$getEntry();
        return itemEntry == null ? new ItemEntry() : itemEntry;
    }

    public static @NotNull ItemEntry defaults() {
        ItemEntry itemEntry = new ItemEntry();
        itemEntry.isDefaulted = true;
        return itemEntry;
    }


    public TintType getTintType() {
        return tintType;
    }

    public ItemEntry model(ItemModel model) {
        this.model = model;
        return this;
    }

    public @Nullable ItemModel getModel() {
        return model;
    }

    public ItemEntry tintType(TintType tintType) {
        this.tintType = tintType;
        return this;
    }


    @Override
    public ItemEntry tags(List<TagKey<Item>> tags) {
        this.tags.addAll(tags);
        return this;
    }

    @Override
    public @NotNull List<TagKey<Item>> getTags() {
        return tags;
    }

    @Override
    public ItemEntry tag(TagKey<Item> tag) {
        this.tags.add(tag);
        return this;
    }

    public ItemEntry isFromBlock(boolean isFromBlock) {
        this.isFromBlock = isFromBlock;
        return this;
    }

    @Override
    public ItemEntry lang(String langValue) {
        this.langValue = langValue;
        return this;
    }

    @Override
    void merge(ItemEntry self, ItemEntry other) {

    }

    public boolean isFromBlock() {
        return isFromBlock;
    }
}
