package net.lixir.vminus.registry.entry;

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

public class ItemEntry extends RegistryEntry<ItemEntry> implements TaggedRegistryEntry<Item, ItemEntry> {
    protected final List<TagKey<Item>> tags = new ArrayList<>();
    protected Model model = Model.UNSET;
    protected TintType tintType;

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
            BlockEntry.Model blockModel = blockEntry.getModel();
            if (blockModel != null)
                itemEntry.model = blockModel.getItemModel();
        }
        return itemEntry;
    }

    public static @NotNull ItemEntry copy(Item item) {
        ItemEntryAccessor accessor = (ItemEntryAccessor) item;
        ItemEntry itemEntry = accessor.vminus$getEntry();
        return itemEntry == null ? new ItemEntry() : itemEntry;
    }


    public TintType getTintType() {
        return tintType;
    }

    public ItemEntry model(Model model) {
        this.model = model;
        return this;
    }

    public @Nullable Model getModel() {
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

    @Override
    public ItemEntry lang(String langValue) {
        this.langValue = langValue;
        return this;
    }

    public enum Model {
        BASIC,
        HANDHELD,
        PANE,
        DOUBLE_PANE,
        PARENT_BLOCK,
        UNSET
    }
}
