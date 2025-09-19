package net.lixir.vminus.api.datagen.block.model;

import net.lixir.vminus.api.datagen.block.BlockData;
import net.lixir.vminus.api.datagen.block.model.provider.VBlockStateProvider;
import net.lixir.vminus.api.datagen.item.model.BuiltInItemModelTypes;
import net.lixir.vminus.api.datagen.item.model.ItemModelType;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

/**
 * Built-in block model types provided by VMinus.
 * These cover most vanilla-like block model cases.
 */
public enum BuiltInBlockModelTypes implements BlockModelType {
    UNSET("unset", (data, provider) -> {
    }, BuiltInItemModelTypes.UNSET),
    NONE("none", (data, provider) -> {
    }, BuiltInItemModelTypes.UNSET),
    AIR("air", (data, provider) -> provider.air(data.block()), BuiltInItemModelTypes.NONE),
    CUBE_ALL("cube_all", (data, provider) -> provider.allSidedCubeWithItem(data.block(), data.renderType()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    LEAVES("leaves", (data, provider) -> provider.leaves(data.block(), data.renderType()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    CROSS("cross", (data, provider) -> provider.cross(data.block(), data.renderType(), data.tintType()), BuiltInItemModelTypes.PANE),
    DOUBLE_CROSS("double_cross", (data, provider) -> provider.doubleCross(data.block(), data.renderType(), data.tintType()), BuiltInItemModelTypes.DOUBLE_PANE),
    STAIRS("stairs", (data, provider) -> provider.stairs(data.block(), data.modelTextureSuffix(), data.modelTextureOverride()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    SLAB("slab", (data, provider) -> provider.slab(data.block(), data.modelTextureSuffix(), data.modelTextureOverride()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    AXIS("axis", (data, provider) -> provider.axis(data.block()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    WOOD("wood", (data, provider) -> provider.woodBlock(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    CUBE_COLUMN("cube_column", (data, provider) -> provider.cubeColumn(data.block()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    FENCE("fence", (data, provider) -> provider.fenceBlock(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.NONE),
    FENCE_GATE("fence_gate", (data, provider) -> provider.fenceGateBlock(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.NONE),
    BUTTON("button", (data, provider) -> provider.button(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.NONE),
    DOOR("door", (data, provider) -> provider.door(data.block()), BuiltInItemModelTypes.BASIC_NOT_BLOCK),
    SIGN("sign", (data, provider) -> provider.sign(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.BASIC_NOT_BLOCK),
    HANGING_SIGN("hanging_sign", (data, provider) -> provider.sign(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.BASIC_NOT_BLOCK),
    TRAPDOOR("trapdoor", (data, provider) -> provider.trapdoor(data.block()), BuiltInItemModelTypes.NONE),
    PRESSURE_PLATE("pressure_plate", (data, provider) -> provider.pressurePlate(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.NONE),
    CUBE_BOTTOM_TOP("cube_bottom_top", (data, provider) -> provider.cubeBottomTop(data.block()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    TORCH("torch", (data, provider) -> provider.torch(data.block()), BuiltInItemModelTypes.PANE),
    WALL_TORCH("wall_torch", (data, provider) -> provider.wallTorch(data.block(), data.modelTextureSuffix()), BuiltInItemModelTypes.NONE),
    LANTERN("lantern", (data, provider) -> provider.lantern(data.block()), BuiltInItemModelTypes.BASIC_NOT_BLOCK),
    WALL("wall", (data, provider) -> provider.wall(data.block(), data.renderType(), data.modelTextureSuffix(), data.modelTextureOverride()), BuiltInItemModelTypes.NONE),
    CARPET("carpet", (data, provider) -> provider.carpet(data.block(), data.modelTextureSuffix(), data.modelTextureOverride()), BuiltInItemModelTypes.FROM_BLOCK_PARENT),
    PINK_PETALS("pink_petals", (data, provider) -> provider.pinkPetals(data.block()), BuiltInItemModelTypes.BASIC_NOT_BLOCK);

    private final String name;
    private final BiConsumer<BlockData, VBlockStateProvider> consumer;
    private final ItemModelType itemModelType;

    BuiltInBlockModelTypes(String name, BiConsumer<BlockData, VBlockStateProvider> consumer, ItemModelType itemModelType) {
        this.name = name;
        this.consumer = consumer;
        this.itemModelType = itemModelType;
    }

    @Override
    public void apply(Block block, VBlockStateProvider provider) {
        if (isEmpty())
            return;
        BlockData data = BlockData.of(block);
        consumer.accept(data, provider);
    }

    @Override
    public BiConsumer<BlockData, VBlockStateProvider> getConsumer() {
        return consumer;
    }

    @Override
    public ItemModelType getItemModelType() {
        return itemModelType;
    }

    @Override
    public String getName() {
        return name;
    }
}
