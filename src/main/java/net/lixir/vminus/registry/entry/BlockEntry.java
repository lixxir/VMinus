package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.TaggedRegistryEntry;
import net.lixir.vminus.registry.TintType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntry extends RegistryEntry<BlockEntry> implements TaggedRegistryEntry<Block, BlockEntry> {
    protected final List<TagKey<Block>> tags = new ArrayList<>();
    protected RenderType renderType = null;
    protected TintType tintType = TintType.UNSET;
    protected Model model = Model.UNSET;
    private boolean isDefaulted = false;

    private BlockEntry() {
    }

    @Contract(" -> new")
    public static @NotNull BlockEntry of() {
        return new BlockEntry();
    }


    public static @NotNull BlockEntry defaults() {
        BlockEntry blockEntry = new BlockEntry();
        blockEntry.isDefaulted = true;
        return blockEntry;
    }

    @SuppressWarnings("unchecked")
    public BlockEntry setDefault(Block block) {
        RegistryEntryDefaults<BlockEntry> accessor = (RegistryEntryDefaults<BlockEntry>) block;
        BlockEntry blockEntry = accessor.vminus$getDefault();
        blockEntry = blockEntry == null ? new BlockEntry() : blockEntry;
        merge(this, blockEntry);
        return blockEntry;
    }

    private void merge(BlockEntry self, BlockEntry other) {
        other.tags(self.tags);
        if (self.tintType != TintType.UNSET)
            other.tintType = self.tintType;
        if (self.model != Model.UNSET)
            other.model = self.model;
        if (self.renderType != null)
            other.renderType = self.renderType;
    }

    public static @NotNull BlockEntry copy(Block block) {
        BlockEntryAccessor accessor = (BlockEntryAccessor) block;
        BlockEntry blockEntry = accessor.vminus$getEntry();
        return blockEntry == null ? new BlockEntry() : blockEntry;
    }

    @Override
    public BlockEntry tag(TagKey<Block> tag) {
        this.tags.add(tag);
        return this;
    }

    public BlockEntry renderType(RenderType renderType) {
        this.renderType = renderType;
        return this;
    }

    public BlockEntry tintType(TintType tintType) {
        this.tintType = tintType;
        return this;
    }

    public BlockEntry model(Model model) {
        this.model = model;
        return this;
    }

    @Override
    public BlockEntry tags(List<TagKey<Block>> tags) {
        this.tags.addAll(tags);
        return this;
    }

    @Override
    public @NotNull List<TagKey<Block>> getTags() {
        return tags;
    }

    public RenderType getRenderType() {
        return renderType != null ? renderType : RenderType.solid();
    }


    public TintType getTintType() {
        return tintType;
    }

    public @Nullable Model getModel() {
        return model;
    }

    @Override
    public BlockEntry lang(String langValue) {
        this.langValue = langValue;
        return this;
    }

    public boolean isDefaulted() {
        return isDefaulted;
    }

    public enum Model {
        ALL_SIDED_CUBE(ItemEntry.Model.PARENT_BLOCK),
        STAIRS(ItemEntry.Model.PARENT_BLOCK),
        AXIS(ItemEntry.Model.PARENT_BLOCK),
        SLAB(ItemEntry.Model.PARENT_BLOCK),
        CROSS(ItemEntry.Model.PANE),
        DOUBLE_CROSS(ItemEntry.Model.DOUBLE_PANE),
        CUBE_COLUMN(ItemEntry.Model.PARENT_BLOCK),
        CUBE_BOTTOM_TOP(ItemEntry.Model.PARENT_BLOCK),
        UNSET(ItemEntry.Model.UNSET);


        private final ItemEntry.Model itemModel;

        Model(ItemEntry.Model itemModel) {
            this.itemModel = itemModel;
        }

        public ItemEntry.Model getItemModel() {
            return itemModel;
        }
    }
}
