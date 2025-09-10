package net.lixir.vminus.api.registry.definition;

import net.lixir.vminus.api.datagen.block.model.BlockModelType;
import net.lixir.vminus.api.datagen.item.model.BuiltInItemModelTypes;
import net.lixir.vminus.api.datagen.item.model.ItemModelType;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.duck.ItemDefinitionDuck;
import net.lixir.vminus.api.tint.BuiltInTintTypes;
import net.lixir.vminus.api.tint.TintType;
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

/**
 * A definition that stores metadata, datagen details, and customization
 * for {@link Item} objects. This includes language keys, model data, tinting,
 * tags, and whether the item originates from a {@link Block}.
 */
public class ItemDefinition extends RegistryDefinition<ItemDefinition, Item> {
    public static final ItemDefinition EMPTY = of();
    protected final @NotNull Set<TagKey<Item>> tags = new HashSet<>();
    protected @NotNull ItemModelType modelType = BuiltInItemModelTypes.UNSET;
    protected @NotNull TintType tintType = BuiltInTintTypes.UNSET;
    private boolean fromBlock = false;

    protected ItemDefinition() {
    }

    /**
     * Creates or retrieves the definition for the given {@link Item}.
     * <p>
     * If the item is a {@link BlockItem}, it will attempt to merge
     * the associated {@link BlockDefinition}'s item entry as well.
     *
     * @param item the item to resolve a definition for
     * @return an existing or new {@link ItemDefinition}
     */
    public static @NotNull ItemDefinition of(Item item) {
        ItemDefinition accessed = ((ItemDefinitionDuck) item).vMinus$getDefinition();
        if (accessed != null) {
            if (item instanceof BlockItem blockItem) {
                BlockDefinition blockDefinition = BlockDefinition.of(blockItem);
                if (!blockDefinition.isEmpty()) {
                    ItemDefinition itemEntry = blockDefinition.getItemDefinition();
                    if (!itemEntry.isEmpty()) {
                        return accessed.merge(itemEntry);
                    }
                }
            }
            return accessed;
        }
        return of();
    }

    /**
     * Creates a fresh, empty {@link ItemDefinition}.
     *
     * @return a new definition
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull ItemDefinition of() {
        return new ItemDefinition();
    }

    /**
     * Creates an {@link ItemDefinition} derived from a {@link BlockDefinition}.
     *
     * @param blockDefinition the block definition, may be null
     * @return a new definition derived from the block
     */
    public static @NotNull ItemDefinition of(@Nullable BlockDefinition blockDefinition) {
        ItemDefinition itemEntry = of();
        if (blockDefinition != null) {
            itemEntry.langKey = null;
            itemEntry.tintType = blockDefinition.tintType;
            BlockModelType blockModelType = blockDefinition.getModelType();
            if (!blockModelType.isEmpty())
                itemEntry.modelType = blockModelType.getItemModelType();
            itemEntry.fromBlock = true;
            itemEntry.merge(blockDefinition.itemDefinition);
        }
        return itemEntry;
    }

    /**
     * Creates an {@link ItemDefinition} derived from a {@link Block}.
     *
     * @param block the block to derive from
     * @return a new definition
     */
    public static @NotNull ItemDefinition of(Block block) {
        return of(BlockDefinition.of(block));
    }

    /**
     * Creates a defaulted {@link ItemDefinition}.
     *
     * @return a defaulted definition
     */
    public static @NotNull ItemDefinition defaults() {
        ItemDefinition itemEntry = new ItemDefinition();
        itemEntry.isDefaulted = true;
        return itemEntry;
    }

    /**
     * @return true if this definition is the sentinel {@link #EMPTY}
     */
    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }

    /**
     * Applies default values from the {@link VRegistry} for the given item.
     *
     * @param item the item to resolve defaults for
     * @return this definition after merging defaults
     */
    public ItemDefinition setDefault(@NotNull Item item) {
        ItemDefinition itemEntry = VRegistry.getDefaultItemDefinition(item);
        merge(itemEntry);
        return this;
    }

    /**
     * @return the tint type of this item
     */
    public @NotNull TintType getTintType() {
        return tintType;
    }

    /**
     * Sets the model data for this item.
     * Only applies during datagen.
     *
     * @param modelType the item model data
     * @return this definition
     */
    public ItemDefinition modelType(@NotNull ItemModelType modelType) {
        if (isDatagen())
            this.modelType = modelType;
        return this;
    }

    public @NotNull ItemModelType getModelType() {
        return modelType;
    }

    public ItemDefinition tint(@NotNull TintType tintType) {
        this.tintType = tintType;
        return this;
    }


    @SafeVarargs
    public final ItemDefinition tags(@NotNull TagKey<Item>... tags) {
        if (isDatagen())
            this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public @NotNull Set<TagKey<Item>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    protected ItemDefinition setIsFromBlock(boolean isFromBlock) {
        this.fromBlock = isFromBlock;
        return this;
    }

    @Override
    public @NotNull ItemDefinition merge(@Nullable ItemDefinition other) {
        if (other == null)
            return this;
        tags.addAll(other.getTags());
        if (this.modelType.isUnset())
            this.modelType = other.modelType;
        if (this.tintType.isUnset())
            this.tintType = other.tintType;
        return this;
    }

    public boolean isFromBlock() {
        return fromBlock;
    }

    @Override
    public String toString() {
        return "ItemEntry{" +
                "tags=" + tags +
                ", model=" + modelType +
                ", tint=" + tintType +
                ", fromBlock=" + fromBlock +
                ", isDefaulted=" + isDefaulted +
                ", langValue='" + langKey + '\'' +
                '}';
    }
}
