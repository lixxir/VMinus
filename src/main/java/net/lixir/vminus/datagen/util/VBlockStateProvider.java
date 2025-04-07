package net.lixir.vminus.datagen.util;

import net.lixir.vminus.block.ModHangingSignBlock;
import net.lixir.vminus.block.ModStandingSignBlock;
import net.lixir.vminus.block.ModWallHangingSignBlock;
import net.lixir.vminus.block.ModWallSignBlock;
import net.lixir.vminus.datagen.util.simple.BlockItemDatagen;
import net.lixir.vminus.datagen.util.simple.DatagenObject;
import net.lixir.vminus.datagen.util.simple.DatagenRegistry;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::blockSets);
        simpleDatagen();
    }

    private void simpleDatagen() {
        for (DatagenObject simpleDatagen : DatagenRegistry.getValuesFromModId(modId)) {
            if (!simpleDatagen.hasModel())
                continue;
            if (simpleDatagen instanceof BlockItemDatagen blockItemSimpleDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemSimpleDatagen.getBlockItemRegistryPair();
                RegistryObject<Block> blockRegistryObject = blockItemPair.blockObject();
                Block block = blockItemPair.block();
                switch (simpleDatagen.getType()) {
                    case ORE -> allSidedCubeWithItem(block, blockRegistryObject);
                    case FLOWER, PLANT -> crossWithItem(block, blockRegistryObject);
                }
            }
        }
    }

    protected void stainedGlassPane(RegistryObject<Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        String path = blockRegistryObject.getId().getPath();
        String namespace = blockRegistryObject.getId().getNamespace();
        String glassPath = path.substring(0, path.indexOf("_pane"));
    }

    protected void allSidedCubeWithItem(RegistryObject<Block> registryObject) {
        allSidedCubeWithItem(registryObject.get(), registryObject);
    }

    protected void allSidedCubeWithItem(Block block, RegistryObject<Block> registryObject) {
        simpleBlockWithItem(block, models().cubeAll(registryObject.getId().getPath(), modLoc("block/" + registryObject.getId().getPath())));
    }

    private void blockSets(BlockSet blockSet) {
        String baseName = blockSet.getBaseBlockName();
        Block block = blockSet.getBaseBlock();
        if (blockSet.getBaseBlockRegistryObject() != null)
            simpleBlockWithItem(block, cubeAll(block));
        if (blockSet.getStairs() != null)
            blockSetStairs(blockSet,(StairBlock) blockSet.getStairs().block(), baseName);
        if (blockSet.getSlab() != null)
            blockSetSlab(blockSet, (SlabBlock) blockSet.getSlab().block(), baseName);
        if (blockSet.getWall() != null)
            blockSetWall(blockSet, (WallBlock) blockSet.getWall().block(), baseName);
        if (blockSet.getFence() != null)
            blockSetFenceBlock(blockSet, (FenceBlock) blockSet.getFence().block(), baseName);
        if (blockSet.getFenceGate() != null)
            blockSetFenceGateBlock(blockSet, (FenceGateBlock) blockSet.getFenceGate().block(), baseName);
        if (blockSet.getPressurePlate() != null)
            blockSetPressurePlateBlock(blockSet, (PressurePlateBlock) blockSet.getPressurePlate().block(), baseName);
        if (blockSet.getButton() != null)
            blockSetButtonBlock(blockSet, (ButtonBlock) blockSet.getButton().block(), baseName);
        if (blockSet.getDoor() != null)
            blockSetDoorBlock(blockSet, (DoorBlock) blockSet.getDoor().block(), baseName);
        if (blockSet.getTrapdoor() != null)
            blockSetTrapdoorBlock(blockSet, (TrapDoorBlock) blockSet.getTrapdoor().block(), baseName);
        if (blockSet.getSign() != null
                && blockSet.getWallSign() != null)
            blockSetSign((ModStandingSignBlock) blockSet.getSign().block(), (ModWallSignBlock) blockSet.getWallSign().block(), baseName);
        if (blockSet.getHangingSign() != null
                && blockSet.getWallHangingSign() != null)
            blockSetHangingSign((ModHangingSignBlock) blockSet.getHangingSign().block(), (ModWallHangingSignBlock) blockSet.getWallHangingSign().block(), baseName);
        if (blockSet.getLog() != null) {
            Block logBlock = blockSet.getLog().block();
            Block strippedLogBlock = blockSet.getStrippedLog().block();
            Block woodBlock = blockSet.getWood().block();
            Block strippedWoodBlock = blockSet.getStrippedWood().block();


            logBlock(((RotatedPillarBlock) logBlock));
            axisBlock(((RotatedPillarBlock) woodBlock), blockTexture(logBlock), blockTexture(logBlock));

            axisBlock(((RotatedPillarBlock) strippedLogBlock), blockTexture(strippedLogBlock),
                    new ResourceLocation(blockTexture(strippedLogBlock) + "_top"));
            axisBlock(((RotatedPillarBlock) strippedWoodBlock), blockTexture(strippedLogBlock),
                    blockTexture(strippedLogBlock));

            blockItem(logBlock);
            blockItem(woodBlock);
            blockItem(strippedLogBlock);
            blockItem(strippedWoodBlock);
        }
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

    public void cubeColumn(Block block) {
        String blockPath = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
        simpleBlock(block, models().withExistingParent(
                        blockPath,
                        new ResourceLocation("minecraft", "block/cube_column"))
                .texture("side", blockTexture(block))
                .texture("end", new ResourceLocation(blockTexture(block).getNamespace(), blockTexture(block).getPath() + "_top"))
        );
    }

    public void cross(Block block) {
        simpleBlock(block, models().cross(blockTexture(block).getPath(),
                blockTexture(block)).renderType("cutout"));
    }

    public void crossWithItem(Block block, RegistryObject<Block> blockRegistryObject) {
        simpleBlock(block, models().cross(blockTexture(block).getPath(),
                blockTexture(block)).renderType("cutout"));
        itemModels().getBuilder(blockRegistryObject.getId().getPath())
                .parent(itemModels().getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("block/" + blockRegistryObject.getId().getPath()));
    }


    public void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(modId +
                ":block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath()));
    }

    private void blockSetStairs(BlockSet blockSet, StairBlock stairBlock, String baseName) {
        stairsBlockWithRenderType(stairBlock, blockSet.getBaseTexture(), "solid");
        itemModels().withExistingParent(baseName + "_stairs", modId + ":block/" + baseName + "_stairs");
    }

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


    private void blockSetSlab(BlockSet blockSet, SlabBlock slabBlock, String baseName) {
        ResourceLocation texture = blockSet.getBaseTexture();
        getVariantBuilder(slabBlock)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM)
                .addModels(new ConfiguredModel(models().slab(baseName + "_slab", texture, texture, texture)))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP)
                .addModels(new ConfiguredModel(models().slabTop(baseName + "_slab_top", texture, texture, texture)))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE)
                .addModels(new ConfiguredModel(models().cubeAll(baseName + "_slab_double", texture)));
        itemModels().withExistingParent(baseName + "_slab", modId + ":block/" + baseName + "_slab");
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
