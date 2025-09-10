package net.lixir.vminus.api.datagen.block.model.provider;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.datagen.block.model.BlockModelType;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.tint.TintType;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.tint.BuiltInTintTypes;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
public abstract class VBlockStateProvider extends BlockStateProvider {
    final public String modId;

    public VBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, String modId) {
        super(output, modId, exFileHelper);
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    protected void registerStatesAndModels() {
        var blocks = VRegistry.fromId(modId).getBlocks();
        for (Block block : blocks) {
            BlockDefinitionDuck accessor = (BlockDefinitionDuck) block;
            BlockDefinition blockDefinition = accessor.vMinus$getDefinition();
            if (blockDefinition == null)
                continue;
            BlockModelType model = blockDefinition.getModelType();
            model.apply(block, this);
        }
    }

    public void carpet(Block block, String modelTextureSuffix, @NotNull ResourceLocation modelTextureOverride) {
        String blockPath = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation texture = modelTextureOverride.equals(BlockDefinition.UNSET_RESOURCE_LOCATION)
                ? textureFromBlock(block, "_carpet", modelTextureSuffix)
                : modelTextureOverride;
        simpleBlock(block, this.models().withExistingParent(blockPath, new ResourceLocation("minecraft", "block/carpet")).texture("wool", texture));
    }

    public void lantern(Block block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ResourceLocation texture = blockTexture(block);

        ModelFile standingModel = models()
                .withExistingParent(name, mcLoc("block/template_lantern"))
                .texture("lantern", texture);

        ModelFile hangingModel = models()
                .withExistingParent(name + "_hanging", mcLoc("block/template_hanging_lantern"))
                .texture("lantern", texture);

        getVariantBuilder(block)
                .partialState().with(LanternBlock.HANGING, false)
                .modelForState().modelFile(standingModel).addModel()
                .partialState().with(LanternBlock.HANGING, true)
                .modelForState().modelFile(hangingModel).addModel();

        itemModels().basicItem(modLoc(name));
    }

    public void torch(RegistryObject<Block> block) {
        torch(block);
    }

    public void torch(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        simpleBlock(block, this.models().withExistingParent(blockPath, new ResourceLocation("block/template_torch")).texture("torch", this.blockTexture(block)));
    }

    @SuppressWarnings("unchecked")
    public void pinkPetals(Block block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        var multipartBuilder = getMultipartBuilder(block);

        Direction[] directions = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        int[] rotations = new int[]{0, 90, 180, 270};

        int maxFlowerAmount = 4;

        Property<Direction> facingProp = (Property<Direction>) block.getStateDefinition().getProperty(BlockStateProperties.FACING.getName());
        Property<Integer> flowerAmountProp = (Property<Integer>) block.getStateDefinition().getProperty("flower_amount");

        for (int flowerAmount = 1; flowerAmount <= maxFlowerAmount; flowerAmount++) {
            for (int petal = 1; petal <= flowerAmount; petal++) {
                String modelName = modId + ":block/" + name + "_" + petal;

                for (int d = 0; d < directions.length; d++) {
                    Direction facing = directions[d];
                    int yRotation = rotations[d];

                    multipartBuilder.part()
                            .modelFile(models().getExistingFile(new ResourceLocation(modelName)))
                            .rotationY(yRotation)
                            .addModel()
                            .condition(facingProp, facing)
                            .condition(flowerAmountProp, flowerAmount);
                }
            }
        }
    }




    public void wallTorch(Block block, String modelTextureSuffix) {
        ResourceLocation wallTorchLocation = BuiltInRegistries.BLOCK.getKey(block);
        String wallTorchPath = wallTorchLocation.getPath();
        String torchName = wallTorchPath.replaceAll(modelTextureSuffix, "");
        Block torch = BuiltInRegistries.BLOCK.get(new ResourceLocation(wallTorchLocation.getNamespace(), torchName));
        ResourceLocation texture = blockTexture(torch);

        ModelFile wallTorchModel = models().torchWall(wallTorchPath, texture);
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(WallTorchBlock.FACING);
            int rotY = (int) dir.getClockWise().toYRot();
            return ConfiguredModel.builder()
                    .modelFile(wallTorchModel)
                    .rotationY(rotY)
                    .build();
        });
    }

    public void stainedGlassPane(Block block) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        String path = resourceLocation.getPath();
        String glassPath = path.substring(0, path.indexOf("_pane"));
    }

    public void allSidedCube(Block block, String renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlock(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(renderType));
    }

    public void tintedAllSidedCube(Block block, String renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlock(block, models().leaves(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(renderType));
    }

    public void allSidedCubeWithItem(Block block, String renderType) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlockWithItem(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType(renderType));
    }

    public void allSidedCubeWithItem(Block block, RegistryObject<Block> registryObject) {
        simpleBlockWithItem(block, models().cubeAll(registryObject.getId().getPath(), modLoc("block/" + registryObject.getId().getPath())));
    }

    public void translucentCubeWithItem(Block block) {
        ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlockWithItem(block, models().cubeAll(resourceLocation.getPath(), modLoc("block/" + resourceLocation.getPath())).renderType("translucent"));
    }

    public void translucentCubeWithItem(Block block, RegistryObject<Block> registryObject) {
        simpleBlockWithItem(block, models().cubeAll(registryObject.getId().getPath(), modLoc("block/" + registryObject.getId().getPath())));
    }

    public void translucentCubeWithItem(RegistryObject<Block> registryObject) {
        translucentCubeWithItem(registryObject.get(), registryObject);
    }

    public void registerLeaves(Block block) {
        simpleBlockWithItem(block,
                models().singleTexture(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(block)));
    }

    public void variedCross(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();

        simpleBlock(block, models().withExistingParent(
                blockPath,
                new ResourceLocation("vminus", "block/varied_cross"))
                .texture("cross", blockTexture(block)));
    }

    public void veinBlock(RegistryObject<Block> registryObject) {
        veinBlock(registryObject.get());
    }

    public void woodBlock(Block block, String modelTextureSuffix) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation texture = textureFromBlock(block, "_wood", modelTextureSuffix);
        ModelFile model = models().cubeAll(name, texture);

        getVariantBuilder(block)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(model).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(model).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(model).addModel();

        simpleBlockItem(block, model);
    }

    public void axis(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        ResourceLocation texture = modLoc("block/" + name);
        ResourceLocation endTexture = modLoc("block/" + name + "_top");
        ModelFile vertical = models().cubeColumn(name, texture, endTexture);
        ModelFile horizontal = models().cubeColumnHorizontal(name + "_horizontal", texture, endTexture);

        getVariantBuilder(block).forAllStates(state -> {
            Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
            return ConfiguredModel.builder()
                    .modelFile(axis == Direction.Axis.Y ? vertical : horizontal)
                    .rotationX(axis == Direction.Axis.X ? 90 : axis == Direction.Axis.Z ? 90 : 0)
                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                    .build();
        });
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

    public void cross(Block block, String renderType, TintType tintType) {
        if (tintType.getName().equals(BuiltInTintTypes.NONE.getName())) {
            simpleBlock(block, models().cross(blockTexture(block).getPath(),
                    blockTexture(block)).renderType(renderType));
        } else {
            ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
            String name = resourceLocation.getPath();
            simpleBlock(block, models().withExistingParent(name, mcLoc("block/tinted_cross"))
                    .texture("cross", blockTexture(block))
                    .renderType(renderType));
        }
    }

    public void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(modId +
                ":block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath()));
    }

    public void doubleCross(Block block, @NotNull String renderType, @NotNull TintType tintType) {
        String modelPath;
        if (tintType.getName().equals(BuiltInTintTypes.NONE.getName())) {
            modelPath = "block/cross";
        } else {
            modelPath = "block/tinted_cross";
        }
        ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(block);
        String name = resourceLocation.getPath();
        ResourceLocation topTexture = modLoc("block/" + name + "_top");
        ResourceLocation bottomTexture = modLoc("block/" + name + "_bottom");

            getVariantBuilder(block)
                    .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                    .modelForState().modelFile(models().withExistingParent(name + "_lower", new ResourceLocation("minecraft", modelPath))
                            .texture("cross", bottomTexture).renderType(renderType)).addModel()
                    .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                    .modelForState().modelFile(models().withExistingParent(name + "_upper", new ResourceLocation("minecraft", modelPath))
                            .texture("cross", topTexture).renderType(renderType)).addModel();

    }

    private static final List<String> AUTO_PLURALIZE = List.of("brick", "plank");

    private ResourceLocation getSuffixedBlockTexture(@NotNull ResourceLocation override, String blockPath, String addSuffix, String removeSuffix) {
        if (!override.equals(BlockDefinition.UNSET_RESOURCE_LOCATION)) {
            return override;
        }
        return modLoc("block/" + formSuffixedTexturePath(blockPath, removeSuffix) + addSuffix);
    }

    private String formSuffixedTexturePath(@NotNull String blockId, String removeSuffix) {
        String base = blockId.endsWith(removeSuffix) ?
                blockId.substring(0, blockId.length() - removeSuffix.length()) :
                blockId;

        for (String pluralize : AUTO_PLURALIZE) {
            if (base.endsWith(pluralize)) {
                return base + "s";
            }
        }

        return base;
    }

    public void wall(Block block, String modelTextureSuffix, ResourceLocation modelTextureOverride) {
        wall(block, "solid", modelTextureSuffix, modelTextureOverride);
    }

    public void wall(Block block, String renderType, String modelTextureSuffix, ResourceLocation modelTextureOverride) {
        String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation texture = getSuffixedBlockTexture(modelTextureOverride, id, modelTextureSuffix, "_wall");
        wallBlockWithRenderType((WallBlock) block, texture, renderType);
        itemModels().wallInventory(id, texture);
    }

    protected ResourceLocation getSuffixedBlockTexture(ResourceLocation modelTextureOverride, Block block, String modelTextureSuffix, String removeSuffix) {
        String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
        return getSuffixedBlockTexture(modelTextureOverride, id, modelTextureSuffix, removeSuffix);
    }

    public void registerDirtGrassBlock(Block block, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation overlayTexture) {
        registerTemplateGrassBlock(block, new ResourceLocation("minecraft", "block/dirt"), topTexture, sideTexture, overlayTexture);
    }

    public void registerTemplateGrassBlock(Block block, ResourceLocation bottomTexture, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation overlayTexture) {
        simpleBlock(block, models().withExistingParent(
                        ForgeRegistries.BLOCKS.getKey(block).getPath(),
                        new ResourceLocation("minecraft", "block/grass_block"))
                .texture("bottom", bottomTexture)
                .texture("particle", bottomTexture)
                .texture("top", topTexture)
                .texture("side", sideTexture)
                .texture("overlay", overlayTexture));
        String blockPath = ForgeRegistries.BLOCKS.getKey(block).getPath();
        itemModels().withExistingParent(blockPath, getModId() + ":block/" + blockPath);
    }

    public void stairs(Block block, String textureSuffix, ResourceLocation modelTextureOverride) {
        stairs(block, "solid", textureSuffix, modelTextureOverride);
    }

    public void stairs(Block block, String renderType, String textureSuffix, ResourceLocation modelTextureOverride) {
        String id = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ResourceLocation texture = getSuffixedBlockTexture(modelTextureOverride, id, textureSuffix, "_stairs");
        stairsBlockWithRenderType((StairBlock) block, texture, renderType);
        itemModels().withExistingParent(id, modLoc("block/" + id));
    }

    public void air(Block block) {
        String path = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        BlockModelBuilder model = models().getBuilder(path);
        simpleBlock(block, model);
    }



    public void slab(Block block, String modelTextureSuffix,  ResourceLocation modelTextureOverride) {
        slab(block, "solid", modelTextureSuffix, modelTextureOverride);
    }

    public void slab(Block block, String renderType, String textureSuffix, ResourceLocation modelTextureOverride) {
        String id = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ResourceLocation texture = getSuffixedBlockTexture(modelTextureOverride, id, textureSuffix, "_slab");

        SlabBlock slabBlock = (SlabBlock) block;

        BlockModelBuilder bottom = models().slab(id, texture, texture, texture).renderType(renderType);
        BlockModelBuilder top = models().slabTop(id + "_top", texture, texture, texture).renderType(renderType);
        BlockModelBuilder doubleSlab = models().cubeAll(id + "_double", texture).renderType(renderType);

        getVariantBuilder(slabBlock)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlab));

        itemModels().withExistingParent(id, modLoc("block/" + id));
    }

    public void door(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = id.getNamespace();
        String path = id.getPath();
        ResourceLocation bottomTexture = new ResourceLocation(namespace, "block/"+ path + "_bottom");
        ResourceLocation topTexture = new ResourceLocation(namespace, "block/"+ path + "_top");
        doorBlock((DoorBlock) block, bottomTexture, topTexture);
    }

    public void trapdoor(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = id.getNamespace();
        String path = id.getPath();
        ResourceLocation texture = new ResourceLocation(namespace, "block/" + path);
        trapdoorBlock((TrapDoorBlock) block, texture, false);

        itemModels().trapdoorBottom(path, texture);
    }

    public void sign(Block block, String modelTextureSuffix) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = id.getNamespace();
        String path = id.getPath();
        if (path.endsWith("_sign"))
            path = path.substring(0, path.indexOf("_sign"));
        if (path.endsWith("_hanging"))
            path = path.substring(0, path.indexOf("_hanging"));
        if (path.endsWith("_wall"))
            path = path.substring(0, path.indexOf("_wall"));
        ResourceLocation texture = new ResourceLocation(namespace, "block/" + path + modelTextureSuffix);

        ModelFile sign = models().sign(name(block), texture);

        simpleBlock(block, sign);
    }

    public void pressurePlate(Block block, String modelTextureSuffix) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

        ResourceLocation texture = textureFromBlock(block, "_pressure_plate", modelTextureSuffix);
        pressurePlateBlock((PressurePlateBlock) block, texture);
        itemModels().pressurePlate(name, texture);
    }

    public void button(Block block, String modelTextureSuffix) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

        ResourceLocation texture = textureFromBlock(block, "_button", modelTextureSuffix);
        buttonBlock((ButtonBlock) block, texture);
        itemModels().buttonInventory(name, texture);
    }

    public void fenceGateBlock(Block block, String modelTextureSuffix) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

        ResourceLocation texture  = textureFromBlock(block, "_fence_gate", modelTextureSuffix);
        fenceGateBlock((FenceGateBlock) block, texture);
        itemModels().fenceGate(name, texture);
    }

    public void fenceBlock(Block block, String modelTextureSuffix) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

        ResourceLocation texture = textureFromBlock(block, "_fence", modelTextureSuffix);
        fenceBlock((FenceBlock) block, texture);
        itemModels().fenceInventory(name, texture);
    }

    public ResourceLocation textureFromBlock(Block block, String lookFor, String modelTextureSuffix) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        String namespace = id.getNamespace();
        String path = id.getPath();
        String trimPath = path.substring(0, path.indexOf(lookFor)) + modelTextureSuffix;
        return new ResourceLocation(namespace, "block/" + trimPath);
    }

    public void simpleBlockWithItem(Block block, ModelFile model) {
        simpleBlock(block, model);
        itemModels().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())
                .parent(model);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public <T> Optional<T> as(@NotNull Class<T> clazz) {
        return clazz.isInstance(this) ? Optional.of(clazz.cast(this)) : Optional.empty();
    }
}
