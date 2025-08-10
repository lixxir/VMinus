package net.lixir.vminus.datagen;

import net.lixir.vminus.datagen.util.VBlockStateProvider;
import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class BlockModel {
    private final BiConsumer<Data, VBlockStateProvider> consumer;
    private final ItemModel itemModel;
    private final String name;

    public BlockModel(String name, BiConsumer<Data, VBlockStateProvider> consumer, @NotNull ItemModel itemModel) {
        this.name = name;
        this.consumer = consumer;
        this.itemModel = itemModel;
    }

    public void apply(Block block, @NotNull BlockEntry blockEntry, VBlockStateProvider provider) {
        BlockModel model = blockEntry.getModel();
        if (model.equals(BlockModel.NONE) || model.equals(BlockModel.UNSET))
            return;
        Data data = Data.of(block, blockEntry);
        consumer.accept(data, provider);
    }

    public static final BlockModel UNSET = new BlockModel("unset", (data, provider) -> {
    }, ItemModel.UNSET);
    public static final BlockModel NONE = new BlockModel("none", (data, provider) -> {
    }, ItemModel.UNSET);
    public static final BlockModel AIR = new BlockModel("air",(data, provider) -> provider.air(data.block()), ItemModel.NONE);
    public static final BlockModel CUBE_ALL = new BlockModel("cube_all",(data, provider) -> provider.allSidedCubeWithItem(data.block(), data.renderType()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel TINTED_CUBE_ALL = new BlockModel("tinted_cube_all",(data, provider) -> provider.tintedAllSidedCube(data.block(), data.renderType()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel CROSS = new BlockModel("cross", (data, provider) -> provider.cross(data.block(), data.renderType(), data.tintType()), ItemModel.PANE);
    public static final BlockModel DOUBLE_CROSS = new BlockModel("double_cross", (data, provider) -> provider.doubleCross(data.block(), data.renderType(), data.tintType()), ItemModel.DOUBLE_PANE);
    public static final BlockModel STAIRS = new BlockModel(
            "stairs",
            (data, provider) -> provider.stairs(
                    data.block(),
                    data.modelTextureSuffix(),
                    data.modelTextureOverride()
            ),
            ItemModel.FROM_BLOCK_PARENT
    );
    public static final BlockModel SLAB = new BlockModel(
            "slab",
            (data, provider) -> provider.slab(
                    data.block(),
                    data.modelTextureSuffix(),
                    data.modelTextureOverride),
            ItemModel.FROM_BLOCK_PARENT
    );
    public static final BlockModel AXIS = new BlockModel("axis", (data, provider) -> provider.axis(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel WOOD = new BlockModel("wood", (data, provider) -> provider.woodBlock(data.block(), data.modelTextureSuffix), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel CUBE_COLUMN = new BlockModel("cube_column", (data, provider) -> provider.cubeColumn(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel FENCE = new BlockModel("fence", (data, provider) -> provider.fenceBlock(data.block(), data.modelTextureSuffix()), ItemModel.NONE);
    public static final BlockModel FENCE_GATE = new BlockModel("fence_gate", (data, provider) -> provider.fenceGateBlock(data.block(), data.modelTextureSuffix()), ItemModel.NONE);
    public static final BlockModel BUTTON = new BlockModel("button", (data, provider) -> provider.button(data.block(), data.modelTextureSuffix()), ItemModel.NONE);
    public static final BlockModel DOOR = new BlockModel("door", (data, provider) -> provider.door(data.block()), ItemModel.BASIC_NOT_BLOCK);
    public static final BlockModel SIGN = new BlockModel("sign", (data, provider) -> provider.sign(data.block(), data.modelTextureSuffix()), ItemModel.BASIC_NOT_BLOCK);
    public static final BlockModel HANGING_SIGN = new BlockModel("hanging_sign", (data, provider) -> provider.sign(data.block(), data.modelTextureSuffix()), ItemModel.BASIC_NOT_BLOCK);
    public static final BlockModel TRAPDOOR = new BlockModel("trapdoor", (data, provider) -> provider.trapdoor(data.block()), ItemModel.NONE);
    public static final BlockModel PRESSURE_PLATE = new BlockModel("pressure_plate", (data, provider) -> provider.pressurePlate(data.block(), data.modelTextureSuffix()), ItemModel.NONE);
    public static final BlockModel CUBE_BOTTOM_TOP = new BlockModel("cube_bottom_top", (data, provider) -> provider.cubeBottomTop(data.block()), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel TORCH = new BlockModel("torch", (data, provider) -> provider.torch(data.block()), ItemModel.PANE);
    public static final BlockModel WALL_TORCH = new BlockModel("torch", (data, provider) -> provider.wallTorch(data.block(), data.modelTextureSuffix()), ItemModel.NONE);
    public static final BlockModel LANTERN = new BlockModel("lantern", (data, provider) -> provider.lantern(data.block()), ItemModel.BASIC_NOT_BLOCK);
    public static final BlockModel WALL = new BlockModel("wall", (data, provider) -> provider.wall(data.block, data.renderType, data.modelTextureSuffix, data.modelTextureOverride), ItemModel.NONE);
    public static final BlockModel CARPET = new BlockModel("carpet", (data, provider) -> provider.carpet(data.block, data.modelTextureSuffix, data.modelTextureOverride), ItemModel.FROM_BLOCK_PARENT);
    public static final BlockModel PINK_PETALS = new BlockModel("pink_petals", (data, provider) -> provider.pinkPetals(data.block), ItemModel.BASIC_NOT_BLOCK);

    public ItemModel getItemModel() {
        return itemModel;
    }

    public record Data(@NotNull Block block, BlockEntry blockEntry, String renderType, TintType tintType, String modelTextureSuffix,
                       ResourceLocation modelTextureOverride) {
        public static @NotNull Data of(Block block, @NotNull BlockEntry blockEntry) {
            String renderType = blockEntry.getRenderType();
            if (renderType.equals("empty") || renderType.equals("unset"))
                renderType = "solid";
            String modelTextureSuffix = blockEntry.getModelTextureSuffix();
            if (modelTextureSuffix.equals("empty") || modelTextureSuffix.equals("unset"))
                modelTextureSuffix = "";
            ResourceLocation modelTextureOverride = blockEntry.getModelTextureOverride();
            TintType tintType = blockEntry.getTintType();
            if (tintType.equals(TintType.UNSET))
                tintType = TintType.NONE;
            return new Data(block, blockEntry, renderType, tintType, modelTextureSuffix, modelTextureOverride);
        }

    }

    @Override
    public String toString() {
        return "BlockModel[name=" + name  + ", itemModel=" + itemModel + ", consumer=" + consumer + "]";
    }
}
