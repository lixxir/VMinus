package net.lixir.vminus.registry.entry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.BlockLootTable;
import net.lixir.vminus.datagen.BlockModel;
import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockEntry extends RegistryEntry<BlockEntry, Block> {
    public static final BlockEntry EMPTY = of();
    protected final @NotNull Set<TagKey<Block>> tags = new HashSet<>();
    protected @NotNull String renderType = "unset";
    protected @NotNull ItemEntry itemEntry = ItemEntry.of();
    protected @NotNull String modelTextureSuffix = "unset";
    protected @NotNull TintType tintType = TintType.UNSET;
    protected @NotNull BlockModel model = BlockModel.UNSET;
    protected @NotNull BlockLootTable lootTable = BlockLootTable.SELF;
    protected @NotNull ResourceLocation modelTextureOverride = UNSET_RESOURCE_LOCATION;
    private boolean isDefaulted = false;

    private BlockEntry() {
    }
    public static @NotNull BlockEntry of(@NotNull BlockItem blockItem) {
        return of(blockItem.getBlock());
    }

    public static @NotNull BlockEntry of(@NotNull Block block) {
        BlockEntry accessed = ((BlockEntryAccessor) block).vminus$getEntry();
        return accessed != null ? accessed : of();
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public @NotNull BlockLootTable getLootTable() {
        return lootTable;
    }

    public BlockEntry lootTable(BlockLootTable lootTable) {
        this.lootTable = lootTable;

        return this;
    }

    public @NotNull ResourceLocation getModelTextureOverride() {
        return modelTextureOverride;
    }

    @Contract(" -> new")
    public static @NotNull BlockEntry of() {
        return new BlockEntry();
    }

    public @NotNull String getModelTextureSuffix() {
        return modelTextureSuffix;
    }

    public static @NotNull BlockEntry defaults() {
        BlockEntry blockEntry = new BlockEntry();
        blockEntry.isDefaulted = true;
        return blockEntry;
    }

    public BlockEntry setDefault(@NotNull Block block) {
        BlockEntry blockEntry = VRegistry.getBlockEntry(block);
        BlockEntry mergedEntry = merge(blockEntry);
        VMinus.LOGGER.debug("Merged Entry:{}", mergedEntry);
        return mergedEntry;
    }

    @Override
    public @NotNull BlockEntry merge(@Nullable BlockEntry other) {
        if (other == null)
            return this;
        this.tags.addAll(other.tags);
        this.itemEntry.merge(other.itemEntry);
        if (this.tintType == TintType.UNSET)
            this.tintType = other.tintType;
        if (this.model == BlockModel.UNSET)
            this.model = other.model;
        if (this.lootTable == BlockLootTable.UNSET)
            this.lootTable = other.lootTable;
        if (this.renderType.equals("unset"))
            this.renderType = other.renderType;
        if (this.modelTextureSuffix.equals("unset"))
            this.modelTextureSuffix = other.modelTextureSuffix;
        if (this.modelTextureOverride.equals(UNSET_RESOURCE_LOCATION))
            this.modelTextureOverride = other.modelTextureOverride;
        return this;
    }

    public @NotNull ItemEntry getItemEntry() {
        return itemEntry;
    }

    public BlockEntry itemEntry(@NotNull ItemEntry itemEntry) {

        itemEntry.setDefaulted(true);
        this.itemEntry = itemEntry;
        return this;
    }

    @SafeVarargs
    public final BlockEntry tags(TagKey<Block>... tags) {
        if (isDatagen())
            this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public @NotNull Set<TagKey<Block>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public BlockEntry modelTextureOverride(@NotNull ResourceLocation modelTextureOverride) {
        if (isDatagen())
            this.modelTextureOverride = modelTextureOverride;
        return this;
    }

    public BlockEntry modelTextureSuffix(@NotNull String modelTextureSuffix) {
        if (isDatagen())
            this.modelTextureSuffix = modelTextureSuffix;
        return this;
    }

    public BlockEntry renderType(@NotNull String renderType) {
        this.renderType = renderType;
        return this;
    }

    public BlockEntry tintType(@NotNull TintType tintType) {
        this.tintType = tintType;
        return this;
    }

    public BlockEntry model(@NotNull BlockModel model) {
        if (isDatagen())
            this.model = model;
        return this;
    }

    public @NotNull String getRenderType() {
        return renderType;
    }


    public @NotNull TintType getTintType() {
        return tintType;
    }

    public @NotNull BlockModel getModel() {
        return model;
    }

    @Override
    public BlockEntry lang(String langValue) {
        if (isDatagen())
            this.lang = langValue;
        return this;
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    @Override
    public String toString() {
        return "BlockEntry{" +
                "tags=" + tags +
                ", renderType='" + renderType + '\'' +
                ", itemEntry=" + itemEntry +
                ", modelTextureSuffix='" + modelTextureSuffix + '\'' +
                ", tintType=" + tintType +
                ", model=" + model +
                ", lootTable=" + lootTable +
                ", modelTextureOverride=" + modelTextureOverride +
                ", isDefaulted=" + isDefaulted +
                ", langValue='" + lang + '\'' +
                '}';
    }
}
