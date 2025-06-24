package net.lixir.vminus.registry;

import net.lixir.vminus.datagen.util.VBlockStateProvider;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;

public class BlockModel {
    private final BiConsumer<Data, VBlockStateProvider> consumer;
    private final ItemModel itemModel;

    public BlockModel(BiConsumer<Data, VBlockStateProvider> consumer, ItemModel itemModel) {
        this.consumer = consumer;
        this.itemModel = itemModel;
    }

    public void apply(Block block, BlockEntry blockEntry, VBlockStateProvider provider) {
        BlockModel model = blockEntry.getModel();
        if (model.equals(BlockModel.EMPTY) || model.equals(BlockModel.UNSET))
            return;
        String renderType = blockEntry.getRenderType();
        if (renderType.equals("empty") || renderType.equals("unset"))
            renderType = "solid";
        TintType tintType = blockEntry.getTintType();
        if (tintType.equals(TintType.UNSET))
            tintType = TintType.EMPTY;
        Data data = new BlockModel.Data(block, blockEntry, renderType, tintType);
        consumer.accept(data, provider);
    }

    public static final BlockModel UNSET = new BlockModel((data, provider) -> {
    }, ItemModel.UNSET);
    public static final BlockModel EMPTY = new BlockModel((data, provider) -> {
    }, ItemModel.UNSET);
    public static final BlockModel CUBE_ALL = new BlockModel((data, provider) -> provider.allSidedCubeWithItem(data.block(), data.renderType()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel TINTED_CUBE_ALL = new BlockModel((data, provider) -> provider.tintedAllSidedCube(data.block(), data.renderType()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel CROSS = new BlockModel((data, provider) -> provider.cross(data.block(), data.renderType(), data.tintType()), ItemModel.PANE);
    public static final BlockModel DOUBLE_CROSS = new BlockModel((data, provider) -> provider.doubleCross(data.block(), data.renderType(), data.tintType()), ItemModel.DOUBLE_PANE);
    public static final BlockModel STAIRS = new BlockModel((data, provider) -> provider.stairs(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel AXIS = new BlockModel((data, provider) -> provider.axisBlock(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel CUBE_COLUMN = new BlockModel((data, provider) -> provider.cubeColumn(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel CUBE_BOTTOM_TOP = new BlockModel((data, provider) -> provider.cubeBottomTop(data.block()), ItemModel.FROM_BLOCK_PARENT);

    public ItemModel getItemModel() {
        return itemModel;
    }

    public record Data(@NotNull Block block, BlockEntry blockEntry, String renderType, TintType tintType) {
    }
}
