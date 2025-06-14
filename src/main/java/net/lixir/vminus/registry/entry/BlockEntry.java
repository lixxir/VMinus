package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.BlockModel;
import net.lixir.vminus.registry.TaggedRegistryEntry;
import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntry extends RegistryEntry<BlockEntry, Block> implements TaggedRegistryEntry<BlockEntry, Block> {
    protected final List<TagKey<Block>> tags = new ArrayList<>();
    protected String renderType = "unset";
    protected TintType tintType = TintType.UNSET;
    protected BlockModel model = BlockModel.UNSET;
    private boolean isDefaulted = false;

    @Contract(" -> new")
    public static @NotNull BlockEntry of() {
        return new BlockEntry();
    }

    public static @NotNull BlockEntry defaults() {
        BlockEntry blockEntry = new BlockEntry();
        blockEntry.isDefaulted = true;
        return blockEntry;
    }

    public BlockEntry setDefault(@NotNull Block block) {
        BlockEntry blockEntry = UnifiedRegistry.getBlockEntry(block.getClass());
        merge(this, blockEntry);
        return this;
    }

    @Override
    public void merge(@NotNull BlockEntry self, @Nullable BlockEntry other) {
        if (other == null)
            return;
        self.tags(other.tags);
        if (self.tintType == TintType.UNSET)
            self.tintType = other.tintType;
        if (self.model == BlockModel.UNSET)
            self.model = other.model;
        if (self.renderType.equals("unset"))
            self.renderType = other.renderType;
    }

    @Override
    public BlockEntry tag(TagKey<Block> tag) {
        this.tags.add(tag);
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

    public @NotNull String getRenderType() {
        return renderType;
    }


    public TintType getTintType() {
        return tintType;
    }

    public @NotNull BlockModel getModel() {
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

    @Override
    public String toString() {
        return "BlockEntry{" +
                "tags=" + tags +
                ", renderType='" + renderType + '\'' +
                ", tintType=" + tintType +
                ", model=" + (model != null ? model.getClass().getSimpleName() : "null") +
                ", isDefaulted=" + isDefaulted +
                ", langValue='" + langValue + '\'' +
                '}';
    }
}
