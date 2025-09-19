package net.lixir.vminus.api.registry.definition;

import net.lixir.vminus.api.datagen.block.loottable.BlockLootTableType;
import net.lixir.vminus.api.datagen.block.loottable.BuiltInBlockLootTableTypes;
import net.lixir.vminus.api.datagen.block.model.BlockModelType;
import net.lixir.vminus.api.datagen.block.model.BuiltInBlockModelTypes;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.api.rendertype.RenderTypeKey;
import net.lixir.vminus.api.tint.TintType;
import net.lixir.vminus.api.tint.BuiltInTintTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the definition of a block for VMinus, holding metadata and datagen information.
 * <p>
 * This includes tags, render type, tinting information, associated item entry, model/texture overrides, and loot table info.
 * BlockDefinitions can be merged to combine metadata from multiple sources.
 */
public class BlockDefinition extends RegistryDefinition<BlockDefinition, Block> {
    private static final BlockDefinition EMPTY = of();
    protected final Set<TagKey<Block>> tags = new HashSet<>();
    protected RenderTypeKey renderTypeKey = RenderTypeKey.UNSET;
    protected ItemDefinition itemDefinition = ItemDefinition.of();
    protected String modelTextureSuffix = "unset";
    protected TintType tintType = BuiltInTintTypes.UNSET;
    protected BlockModelType modelType = BuiltInBlockModelTypes.UNSET;
    protected BlockLootTableType lootTableType = BuiltInBlockLootTableTypes.UNSET;
    protected ResourceLocation modelTextureOverride = UNSET_RESOURCE_LOCATION;

    protected BlockDefinition() {
    }

    /**
     * Creates a new, empty block definition.
     */
    public static @NotNull BlockDefinition of() {
        return new BlockDefinition();
    }

    /**
     * Returns the BlockDefinition associated with a block.
     * If none exists, returns an empty definition.
     *
     * @param block the block to retrieve the definition for
     * @return the BlockDefinition associated with the block
     */
    public static @NotNull BlockDefinition of(@NotNull Block block) {
        BlockDefinition accessed = BlockDefinitionDuck.of(block).vMinus$getDefinition();
        return accessed != null ? accessed : of();
    }

    /**
     * Returns the BlockDefinition associated with a block item.
     *
     * @param item the BlockItem to retrieve the definition for
     * @return the BlockDefinition of the item's block
     */
    public static @NotNull BlockDefinition of(@NotNull BlockItem item) {
        return of(item.getBlock());
    }

    /**
     * Returns a defaulted BlockDefinition.
     *
     * @return a default BlockDefinition used for merging
     */
    public static @NotNull BlockDefinition defaults() {
        BlockDefinition def = new BlockDefinition();
        def.isDefaulted = true;
        return def;
    }

    /**
     * Merges another BlockDefinition into this one.
     * Fields not yet set in this definition are replaced by values from the other.
     *
     * @param other the BlockDefinition to merge from
     * @return this BlockDefinition after merging
     */
    @Override
    public @NotNull BlockDefinition merge(@Nullable BlockDefinition other) {
        if (other == null)
            return this;

        tags.addAll(other.tags);
        itemDefinition.merge(other.itemDefinition);
        tintType = tintType.isUnset() ? other.tintType : tintType;
        modelType = modelType.isUnset() ? other.modelType : modelType;
        lootTableType = lootTableType.isUnset() ? other.lootTableType : lootTableType;
        renderTypeKey = renderTypeKey.isUnset() ? other.renderTypeKey : renderTypeKey;
        modelTextureSuffix = modelTextureSuffix.equals("unset") ? other.modelTextureSuffix : modelTextureSuffix;
        modelTextureOverride = modelTextureOverride.equals(UNSET_RESOURCE_LOCATION) ? other.modelTextureOverride : modelTextureOverride;

        return super.merge(other);
    }

    /**
     * Sets this BlockDefinition with the default for the given block.
     *
     * @param block the block to use as default
     * @return this BlockDefinition after merging the default
     */
    @Override
    public @NotNull BlockDefinition setDefault(@NotNull Block block) {
        return this.merge(VRegistry.getDefaultBlockDefinition(block));
    }

    @Override
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public @NotNull Set<TagKey<Block>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public @NotNull ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public @NotNull BlockLootTableType getLootTableType() {
        return lootTableType;
    }

    public @NotNull BlockModelType getModelType() {
        return modelType;
    }

    public @NotNull TintType getTintType() {
        return tintType;
    }

    public @NotNull RenderTypeKey getRenderTypeKey() {
        return renderTypeKey;
    }

    public @NotNull String getModelTextureSuffix() {
        return modelTextureSuffix.equals("unset") ? "" : modelTextureSuffix;
    }

    public @NotNull ResourceLocation getModelTextureOverride() {
        return modelTextureOverride;
    }

    public BlockDefinition itemDefinition(@NotNull ItemDefinition itemDefinition) {
        itemDefinition.setDefaulted();
        this.itemDefinition = itemDefinition;
        return this;
    }

    public BlockDefinition lootTableType(@NotNull BlockLootTableType lootTableType) {
        this.lootTableType = lootTableType;
        return this;
    }

    @SafeVarargs
    public final BlockDefinition tags(TagKey<Block>... tags) {
        if (isDatagen()) 
            this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public BlockDefinition modelType(@NotNull BlockModelType model) {
        if (isDatagen()) 
            this.modelType = model;
        return this;
    }

    public BlockDefinition tintType(@NotNull TintType tint) {
        this.tintType = tint;
        return this;
    }

    public BlockDefinition renderType(@NotNull RenderTypeKey type) {
        this.renderTypeKey = type;
        return this;
    }

    public BlockDefinition modelTextureSuffix(@NotNull String suffix) {
        if (isDatagen())
            this.modelTextureSuffix = suffix;
        return this;
    }

    public BlockDefinition modelTextureOverride(@NotNull ResourceLocation override) {
        if (isDatagen())
            this.modelTextureOverride = override;
        return this;
    }

    @Override
    public String toString() {
        return "BlockDefinition{" +
                "tags=" + tags +
                ", renderType='" + renderTypeKey + '\'' +
                ", itemEntry=" + itemDefinition +
                ", modelTextureSuffix='" + modelTextureSuffix + '\'' +
                ", tintType=" + tintType +
                ", model=" + modelType +
                ", lootTable=" + lootTableType +
                ", modelTextureOverride=" + modelTextureOverride +
                ", isDefaulted=" + isDefaulted +
                ", langValue='" + langKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BlockDefinition other))
            return false;

        return isDefaulted == other.isDefaulted &&
                tags.equals(other.tags) &&
                renderTypeKey.equals(other.renderTypeKey) &&
                itemDefinition.equals(other.itemDefinition) &&
                modelTextureSuffix.equals(other.modelTextureSuffix) &&
                tintType.equals(other.tintType) &&
                modelType.equals(other.modelType) &&
                lootTableType.equals(other.lootTableType) &&
                modelTextureOverride.equals(other.modelTextureOverride) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + renderTypeKey.hashCode();
        result = 31 * result + itemDefinition.hashCode();
        result = 31 * result + modelTextureSuffix.hashCode();
        result = 31 * result + tintType.hashCode();
        result = 31 * result + modelType.hashCode();
        result = 31 * result + lootTableType.hashCode();
        result = 31 * result + modelTextureOverride.hashCode();
        result = 31 * result + Boolean.hashCode(isDefaulted);
        return result;
    }
}
