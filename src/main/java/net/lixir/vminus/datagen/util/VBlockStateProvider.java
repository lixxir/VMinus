package net.lixir.vminus.datagen.util;

import net.lixir.vminus.mixins.client.RenderStateShardAccessor;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Objects;

public class VBlockStateProvider extends BlockStateProvider {
    final private String modId;

    public VBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, String modId) {
        super(output, modId, exFileHelper);
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void registerStatesAndModels() {
        for (Block block : UnifiedRegistry.fromId(modId).getBlocks()) {
            BlockEntryAccessor accessor = (BlockEntryAccessor) block;
            BlockEntry blockEntry = accessor.vminus$getEntry();
            if (blockEntry == null)
                continue;
            BlockEntry.Model model = blockEntry.getModel();
            if (model == null || model == BlockEntry.Model.UNSET)
                continue;
            RenderType renderType = blockEntry.getRenderType();
            TintType tintType = blockEntry.getTintType();
            switch (model) {
                case ALL_SIDED_CUBE -> {
                    if (tintType == null || tintType == TintType.UNSET)
                        allSidedCube(block, renderType);
                    else
                        tintedAllSidedCube(block, renderType);
                }
                case STAIRS -> stairs(block, renderType);
                case SLAB -> slab(block, renderType);
                case CROSS -> {
                    if (tintType != null)
                        tintedCross(block, renderType);
                    else
                        cross(block, renderType);
                }
                case DOUBLE_CROSS -> {
                    if (tintType != null)
                        tintedDoubleCross(block, renderType);
                    else
                        doubleCross(block, renderType);
                }
                case AXIS -> axisBlock(block);
                case CUBE_COLUMN -> cubeColumn(block);
                case CUBE_BOTTOM_TOP -> cubeBottomTop(block);
            }
        }
    }


    protected void stainedGlassPane(Block block) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        String path = resourceLocation.getPath();
        String glassPath = path.substring(0, path.indexOf("_pane"));
    }

    protected void allSidedCube(Block block, RenderType renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlock(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(((RenderStateShardAccessor) renderType).getName()));
    }

    protected void tintedAllSidedCube(Block block, RenderType renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlock(block, models().leaves(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(((RenderStateShardAccessor) renderType).getName()));
    }

    protected void allSidedCubeWithItem(Block block, RenderType renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlockWithItem(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(((RenderStateShardAccessor) renderType).getName()));
    }

    protected void allSidedCubeWithItem(Block block, RegistryObject<Block> registryObject) {
        simpleBlockWithItem(block, models().cubeAll(registryObject.getId().getPath(), modLoc("block/" + registryObject.getId().getPath())));
    }

    protected void translucentCubeWithItem(Block block) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlockWithItem(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType("translucent"));
    }

    protected void translucentCubeWithItem(Block block, RegistryObject<Block> registryObject) {
        simpleBlockWithItem(block, models().cubeAll(registryObject.getId().getPath(), modLoc("block/" + registryObject.getId().getPath())));
    }

    protected void translucentCubeWithItem(RegistryObject<Block> registryObject) {
        translucentCubeWithItem(registryObject.get(), registryObject);
    }

    private void registerLeaves(Block block) {
        simpleBlockWithItem(block,
                models().singleTexture(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(block)));
    }

    protected void woolCarpetBlock(Block block) {
        ResourceLocation resourceLocation = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
        String blockPath = resourceLocation.getPath();
        String colorName = resourceLocation.getPath().substring(0, blockPath.indexOf("_carpet"));
        simpleBlock(block, models().withExistingParent(
                        blockPath,
                        new ResourceLocation("minecraft", "block/carpet"))
                .texture("wool", new ResourceLocation(resourceLocation.getNamespace(), "block/" +  colorName + "_wool")));
        itemModels().withExistingParent(colorName + "_carpet", modId + ":block/" + colorName + "_carpet");
    }

    protected void variedCross(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        simpleBlock(block, models().withExistingParent(
                blockPath,
                new ResourceLocation("vminus", "block/varied_cross"))
                .texture("cross", blockTexture(block)));
    }

    public void veinBlock(RegistryObject<Block> registryObject) {
        veinBlock(registryObject.get());
    }


    public void axisBlock(Block block) {
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        String path = name.getPath();

        ModelFile logY = models().cubeColumn(path,
                blockTexture(block),
                new ResourceLocation(blockTexture(block).getNamespace(), blockTexture(block).getPath() + "_top"));
        ModelFile logX = models().cubeColumnHorizontal(path + "_horizontal",
                blockTexture(block),
                new ResourceLocation(blockTexture(block).getNamespace(), blockTexture(block).getPath() + "_top"));

        getVariantBuilder(block)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(logY).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(logX).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(logY).addModel();

        simpleBlockItem(block, logY);
    }

    public void veinBlock(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        simpleBlock(block, models().withExistingParent(
                        blockPath,
                        new ResourceLocation("minecraft", "block/sculk_vein"))
                .texture("sculk_vein", blockTexture(block))
        );
    }

    public void cubeColumn(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        simpleBlock(block, models().withExistingParent(
                        blockPath,
                        new ResourceLocation("minecraft", "block/cube_column"))
                .texture("side", blockTexture(block))
                .texture("end", new ResourceLocation(blockTexture(block).getNamespace(), blockTexture(block).getPath() + "_top"))
        );
    }

    public void cubeBottomTop(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        ResourceLocation baseTexture = blockTexture(block);

        simpleBlock(block, models().withExistingParent(
                        blockPath,
                        new ResourceLocation("minecraft", "block/cube_bottom_top"))
                .texture("top", new ResourceLocation(baseTexture.getNamespace(), baseTexture.getPath() + "_top"))
                .texture("bottom", new ResourceLocation(baseTexture.getNamespace(), baseTexture.getPath() + "_bottom"))
                .texture("side", baseTexture)
        );
    }

    public void cross(Block block, RenderType renderType) {
        simpleBlock(block, models().cross(blockTexture(block).getPath(),
                blockTexture(block)).renderType(((RenderStateShardAccessor) renderType).getName()));
    }

    public void tintedCross(Block block, RenderType renderType) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        simpleBlock(block, models().withExistingParent(name, mcLoc("block/tinted_cross"))
                .texture("cross", blockTexture(block))
                .renderType(((RenderStateShardAccessor) renderType).getName()));
    }


    public void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(modId +
                ":block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath()));
    }

    public void tintedDoubleCross(Block block, RenderType renderType) {
        doubleCross(block, renderType, "block/tinted_cross");
    }

    public void doubleCross(Block block, RenderType renderType) {
        doubleCross(block, renderType, "block/cross");
    }

    public void doubleCross(Block block, RenderType renderType, String modelPath) {
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        ResourceLocation topTexture = modLoc("block/" + name + "_top");
        ResourceLocation bottomTexture = modLoc("block/" + name + "_bottom");

        getVariantBuilder(block)
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                .modelForState().modelFile(models().withExistingParent(name + "_lower", new ResourceLocation("minecraft", modelPath))
                        .texture("cross", bottomTexture).renderType(((RenderStateShardAccessor) renderType).getName())).addModel()
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                .modelForState().modelFile(models().withExistingParent(name + "_upper", new ResourceLocation("minecraft", modelPath))
                        .texture("cross", topTexture).renderType(((RenderStateShardAccessor) renderType).getName())).addModel();
    }


    private void stairs(Block block) {
        stairs(block, null);
    }

    private void stairs(Block block, @Nullable RenderType renderType) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        String baseTexture = name.endsWith("_stairs") ? name.substring(0, name.length() - "_stairs".length()) : name;
        stairsBlockWithRenderType((StairBlock) block, modLoc("block/" + baseTexture), renderType != null ? ((RenderStateShardAccessor) renderType).getName() : "solid");
        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void slab(Block block) {
        slab(block, null);
    }

    private void slab(Block block, @Nullable RenderType renderType) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        String baseTexture = name.endsWith("_slab") ? name.substring(0, name.length() - "_slab".length()) : name;
        ResourceLocation texture = modLoc("block/" + baseTexture);

        SlabBlock slabBlock = (SlabBlock) block;

        BlockModelBuilder bottom = models().slab(name, texture, texture, texture);
        BlockModelBuilder top = models().slabTop(name + "_top", texture, texture, texture);
        BlockModelBuilder doubleSlab = models().cubeAll(name + "_double", texture);

        if (renderType != null) {
            bottom.renderType(((RenderStateShardAccessor) renderType).getName());
            top.renderType(((RenderStateShardAccessor) renderType).getName());
            doubleSlab.renderType(((RenderStateShardAccessor) renderType).getName());
        }

        getVariantBuilder(slabBlock)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM)
                .addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP)
                .addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE)
                .addModels(new ConfiguredModel(doubleSlab));

        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    /*
    private void blockSetSign(ModStandingSignBlock standingSignBlock, ModWallSignBlock wallSignBlock, String baseName) {
        signBlock(standingSignBlock, wallSignBlock, new ResourceLocation(modId, "entity/signs/" + baseName));
        itemModels().basicItem(new ResourceLocation(modId,  baseName + "_sign"));
    }

    private void blockSetHangingSign(ModHangingSignBlock signBlock, ModWallHangingSignBlock wallSignBlock, String baseName) {
        ModelFile sign = models().sign(name(signBlock), new ResourceLocation(modId, "entity/signs/hanging/" + baseName));
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
        itemModels().basicItem(new ResourceLocation(modId, baseName + "_hanging_sign"));
    }



    private void blockSetWall(BlockSet blockSet, WallBlock wallBlock, String baseName) {
        ResourceLocation texture = blockSet.getBaseTexture();
        wallBlock(wallBlock, baseName, texture);
        itemModels().wallInventory(baseName + "_wall", texture);
    }

    private void blockSetFenceBlock(BlockSet blockSet, FenceBlock block, String baseName) {
        fenceBlock(block, baseName, blockSet.getBaseTexture());
        itemModels().fenceInventory(baseName + "_fence", blockSet.getBaseTexture());
    }

    private void blockSetFenceGateBlock(BlockSet blockSet, FenceGateBlock block, String baseName) {
        fenceGateBlock(block, baseName, blockSet.getBaseTexture());
        itemModels().withExistingParent(baseName + "_fence_gate", modId + ":block/" + baseName + "_fence_gate");
    }

    private void blockSetButtonBlock(BlockSet blockSet, ButtonBlock block, String baseName) {
        buttonBlock(block, blockSet.getBaseTexture());
        itemModels().buttonInventory(baseName + "_button", blockSet.getBaseTexture());
    }

    private void blockSetPressurePlateBlock(BlockSet blockSet, PressurePlateBlock block, String baseName) {
        pressurePlateBlock(block, blockSet.getBaseTexture());
        itemModels().withExistingParent(baseName + "_pressure_plate", modId + ":block/" + baseName + "_pressure_plate");
    }

    private void blockSetDoorBlock(BlockSet blockSet, DoorBlock block, String baseName) {
        ResourceLocation bottomTexture = new ResourceLocation(blockSet.getModId(), "block/" + baseName + "_door_bottom");
        ResourceLocation topTexture = new ResourceLocation(blockSet.getModId(), "block/" + baseName + "_door_top");
        doorBlockWithRenderType(block, bottomTexture, topTexture, "cutout_mipped");
        itemModels().basicItem(new ResourceLocation(modId ,baseName + "_door"));
    }

    private void blockSetTrapdoorBlock(BlockSet blockSet, TrapDoorBlock block, String baseName) {
        String trapdoorName =  baseName + "_trapdoor";
        ResourceLocation trapdoorTexture = new ResourceLocation(blockSet.getModId(), "block/" + trapdoorName);

        trapdoorBlockWithRenderType(block, trapdoorTexture, true, "cutout");
        itemModels().trapdoorBottom(trapdoorName, trapdoorTexture);
    }

     */

    public void simpleBlockWithItem(Block block, ModelFile model) {
        simpleBlock(block, model);
        itemModels().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())
                .parent(model);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
