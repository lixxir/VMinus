package net.lixir.vminus.registry.entry;

import net.lixir.vminus.datagen.BlockModel;
import net.lixir.vminus.datagen.ItemModel;
import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.accessor.ItemEntryAccessor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ItemEntry extends RegistryEntry<ItemEntry, Item> {
    public static final ItemEntry EMPTY = of();
    protected final @NotNull Set<TagKey<Item>> tags = new HashSet<>();
    protected @NotNull ItemModel model = ItemModel.UNSET;
    protected @NotNull TintType tint = TintType.UNSET;
    private boolean fromBlock = false;

    private ItemEntry() {}

    public static @NotNull ItemEntry of(Item item) {
        ItemEntry accessed = ((ItemEntryAccessor) item).vminus$getEntry();
        if (accessed != null) {
            if (item instanceof BlockItem blockItem) {
                BlockEntry blockEntry = BlockEntry.of(blockItem);
                if (!blockEntry.isEmpty()) {
                    ItemEntry itemEntry = blockEntry.getItemEntry();
                    if (!itemEntry.isEmpty()) {
                        return accessed.merge(itemEntry);
                    }
                }
            }
            return accessed;
        }
        return of();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull ItemEntry of() {
        return new ItemEntry();
    }

    public static @NotNull ItemEntry from(@Nullable BlockEntry blockEntry) {
        ItemEntry itemEntry = new ItemEntry();
        if (blockEntry != null) {
            itemEntry.lang = null;
            itemEntry.tint = blockEntry.tintType;
            BlockModel blockModel = blockEntry.getModel();
            if (!blockModel.equals(BlockModel.NONE) && !blockModel.equals(BlockModel.UNSET))
                itemEntry.model = blockModel.getItemModel();
            itemEntry.fromBlock = true;
            itemEntry.merge(blockEntry.itemEntry);
        }
        return itemEntry;
    }

    public static @NotNull ItemEntry from(Block block) {
        return from(((BlockEntryAccessor) block).vminus$getEntry());
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public static @NotNull ItemEntry defaults() {
        ItemEntry itemEntry = new ItemEntry();
        itemEntry.isDefaulted = true;
        return itemEntry;
    }

    public ItemEntry setDefault(@NotNull Item item) {
        ItemEntry itemEntry = VRegistry.getItemEntry(item);
        merge(itemEntry);
        return this;
    }

    public @NotNull TintType getTint() {
        return tint;
    }

    public ItemEntry model(@NotNull ItemModel model) {
        if (isDatagen())
            this.model = model;
        return this;
    }

    public @NotNull ItemModel getModel() {
        return model;
    }

    public ItemEntry tint(@NotNull TintType tintType) {
        this.tint = tintType;
        return this;
    }


    @SafeVarargs
    public final ItemEntry tags(@NotNull TagKey<Item>... tags) {
        if (isDatagen())
            this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public @NotNull Set<TagKey<Item>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public ItemEntry isFromBlock(boolean isFromBlock) {
        this.fromBlock = isFromBlock;
        return this;
    }

    @Override
    public ItemEntry lang(String langValue) {
        if (isDatagen())
            this.lang = langValue;
        return this;
    }

    @Override
    public @NotNull ItemEntry merge(@Nullable ItemEntry other) {
        if (other == null)
            return this;
        tags.addAll(other.getTags());
        if (this.model == ItemModel.UNSET)
            this.model = other.model;
        if (this.tint == TintType.UNSET)
            this.tint = other.tint;
        return this;
    }

    public boolean isFromBlock() {
        return fromBlock;
    }

    @Override
    public String toString() {
        return "ItemEntry{" +
                "tags=" + tags +
                ", model=" + model +
                ", tint=" + tint +
                ", fromBlock=" + fromBlock +
                ", isDefaulted=" + isDefaulted +
                ", langValue='" + lang + '\'' +
                '}';
    }
}
