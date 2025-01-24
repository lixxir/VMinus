package net.lixir.vminus.datagen.blockset;


import net.lixir.vminus.VMinus;
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

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockSetStateProvider extends BlockStateProvider {
    final private String modId;

    public BlockSetStateProvider(PackOutput output, ExistingFileHelper exFileHelper, String modId) {
        super(output, modId, exFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerStatesAndModels() {
        BlockSet.BLOCK_SETS.stream()
                .filter(blockSet -> blockSet.getModId().equals(modId))
                .forEach(this::registerBlockSetStates);
    }


    private void registerBlockSetStates(BlockSet blockSet) {
        String baseName = blockSet.getBaseName();

        RegistryObject<Block> baseBlock;
        ResourceLocation baseBlockKey;
        if (blockSet.isWoodSet()) {
            baseBlockKey = new ResourceLocation(modId, baseName + "_planks");
        } else {
            baseBlockKey = new ResourceLocation(modId, baseName);
        }
        baseBlock = RegistryObject.create(baseBlockKey, ForgeRegistries.BLOCKS);

        String alternateBaseName = blockSet.getAlternateBaseNameRaw();
        BlockSet.RenderType renderType = blockSet.getRenderType();
        if (blockSet.hasBase())
            simpleBlockWithItem(baseBlock.get(), cubeAll(baseBlock.get()));
        if (blockSet.hasStairs())
            registerStairsBlock(baseName, renderType, alternateBaseName);
        if (blockSet.hasSlab())
            registerSlabBlock(baseName, renderType, alternateBaseName);
        if (blockSet.hasWall())
            registerWallBlock(baseName, alternateBaseName);
        if (blockSet.hasFence())
            registerFenceBlock(baseName, alternateBaseName);
        if (blockSet.hasFenceGate())
            registerFenceGateBlock(baseName, alternateBaseName);
        if (blockSet.hasPressurePlate())
            registerPressurePlateBlock(baseName, alternateBaseName);
        if (blockSet.hasButton())
            registerButtonBlock(baseName, alternateBaseName);
        if (blockSet.hasTrapdoor())
            registerTrapdoorBlock(baseName);
        if (blockSet.hasDoor())
            registerDoorBlock(baseName);
        if (blockSet.hasSign()) {
            StandingSignBlock standingSignBlock = (StandingSignBlock) blockSet.getStandingSignBlock();
            WallSignBlock wallSignBlock = (WallSignBlock) blockSet.getWallSignBlock();
            registerSignBlock(standingSignBlock, wallSignBlock, baseName, baseBlock.get());
        }
        if (blockSet.hasHangingSign()) {
            Block standingSignBlock = blockSet.getHangingSignBlock();
            Block wallSignBlock = blockSet.getWallHangingSignBlock();
            registerHangingSignBlock(standingSignBlock, wallSignBlock, baseName);
        }
        if (blockSet.hasLog()) {
            Block logBlock = blockSet.getLogBlock();
            Block strippedLogBlock = blockSet.getStrippedLogBlock();
            Block woodBlock = blockSet.getWoodBlock();
            Block strippedWoodBlock = blockSet.getStrippedWoodBlock();


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
        if (blockSet.hasCracked())
            registerCrackedBlock(baseName);
        if (blockSet.hasLeaves()) {
            Block leavesBlock = blockSet.getLeavesBlock();
            registerLeaves(leavesBlock);
        }
    }


    private void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(modId +
                ":block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath()));
    }


    private ResourceLocation getTextureLocation(String baseName, @Nullable String alternateBaseName) {
        ResourceLocation resourceLocation;
        String alternateNamespace = BlockSet.getAlternateNamespace(alternateBaseName);
        if (alternateNamespace != null && !alternateNamespace.isEmpty()) {
            String fixedAlternateBaseName = BlockSet.getAlternateBaseName(alternateBaseName);
            resourceLocation = new ResourceLocation(alternateNamespace, "block/" + (fixedAlternateBaseName != null ? fixedAlternateBaseName : baseName));
        } else {
            resourceLocation = modLoc("block/" + baseName);
        }
        return resourceLocation;
    }

    private void registerWallBlock(String baseName, @Nullable String alternateBaseName) {
        String wallName = BlockSet.correctBaseName(baseName) + "_wall";
        ResourceLocation key = new ResourceLocation(modId, wallName);
        RegistryObject<Block> wallBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (wallBlock.isPresent()) {
            Block block = wallBlock.get();
            if (block instanceof WallBlock) {
                wallBlock(block, baseName, alternateBaseName);
                itemModels().wallInventory(wallName, getTextureLocation(baseName, alternateBaseName));
            } else {
                VMinus.LOGGER.warn("Wall block is not a WallBlock: {}", wallName);
            }
        } else {
            VMinus.LOGGER.warn("Wall block not found: {}", wallName);
        }
    }

    private void registerSlabBlock(String baseName, BlockSet.RenderType renderType, @Nullable String alternateBaseName) {
        String slabName = BlockSet.correctBaseName(baseName) + "_slab";
        ResourceLocation key = new ResourceLocation(modId, slabName);
        RegistryObject<Block> slabBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (slabBlock.isPresent()) {
            Block block = slabBlock.get();
            if (block instanceof SlabBlock) {
                slabBlock(baseName, renderType, alternateBaseName);
                itemModels().withExistingParent(slabName, modId + ":block/" + slabName);
            } else {
                VMinus.LOGGER.warn("BlockSet Slab block is not a SlabBlock: {}", slabName);
            }
        } else {
            VMinus.LOGGER.warn("BlockSet Slab block not found: {}", slabName);
        }
    }

    private void registerStairsBlock(String baseName, BlockSet.RenderType renderType, @Nullable String alternateBaseName) {
        String stairsName = BlockSet.correctBaseName(baseName) + "_stairs";
        ResourceLocation key = new ResourceLocation(modId, stairsName);
        RegistryObject<Block> stairsBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (stairsBlock.isPresent()) {
            Block block = stairsBlock.get();
            if (block instanceof StairBlock) {
                stairsBlock(block, baseName, renderType, alternateBaseName);
                itemModels().withExistingParent(stairsName, modId + ":block/" + stairsName);
            } else {
                VMinus.LOGGER.warn("Stairs block is not a StairBlock: {}", stairsName);
            }
        } else {
            VMinus.LOGGER.warn("Stairs block not found: {}", stairsName);
        }
    }

    private void registerFenceBlock(String baseName, @Nullable String alternateBaseName) {
        String fenceName = BlockSet.correctBaseName(baseName) + "_fence";
        ResourceLocation key = new ResourceLocation(modId, fenceName);
        RegistryObject<Block> fenceBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (fenceBlock.isPresent()) {
            Block block = fenceBlock.get();
            if (block instanceof FenceBlock) {
                fenceBlock(block, baseName, alternateBaseName);
                itemModels().fenceInventory(fenceName, getTextureLocation(baseName, alternateBaseName));
            } else {
                VMinus.LOGGER.warn("Fence block is not a FenceBlock: {}", fenceName);
            }
        } else {
            VMinus.LOGGER.warn("Fence block not found: {}", fenceName);
        }
    }

    private void registerFenceGateBlock(String baseName, @Nullable String alternateBaseName) {
        String fenceGateName = BlockSet.correctBaseName(baseName) + "_fence_gate";
        ResourceLocation key = new ResourceLocation(modId, fenceGateName);
        RegistryObject<Block> fenceGateBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);


        if (fenceGateBlock.isPresent()) {
            Block block = fenceGateBlock.get();
            if (block instanceof FenceGateBlock) {
                fenceGateBlock(block, baseName, alternateBaseName);
                itemModels().withExistingParent(fenceGateName, modId + ":block/" + fenceGateName);
            } else {
                VMinus.LOGGER.warn("Fence gate block is not a FenceGateBlock: {}", fenceGateName);
            }
        } else {
            VMinus.LOGGER.warn("Fence gate block not found: {}", fenceGateName);
        }
    }

    private void registerPressurePlateBlock(String baseName, @Nullable String alternateBaseName) {
        String pressurePlateName = BlockSet.correctBaseName(baseName) + "_pressure_plate";
        ResourceLocation key = new ResourceLocation(modId, pressurePlateName);
        RegistryObject<Block> pressurePlateBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (pressurePlateBlock.isPresent()) {
            Block block = pressurePlateBlock.get();
            if (block instanceof PressurePlateBlock) {
                pressurePlateBlock(block, baseName, alternateBaseName);
                itemModels().withExistingParent(pressurePlateName, modId + ":block/" + pressurePlateName);
            } else {
                VMinus.LOGGER.warn("Pressure plate block is not a PressurePlateBlock: {}", pressurePlateName);
            }
        } else {
            VMinus.LOGGER.warn("Pressure plate block not found: {}", pressurePlateName);
        }
    }

    private void registerButtonBlock(String baseName, @Nullable String alternateBaseName) {
        String buttonName = BlockSet.correctBaseName(baseName) + "_button";
        ResourceLocation key = new ResourceLocation(modId, buttonName);
        RegistryObject<Block> buttonBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (buttonBlock.isPresent()) {
            Block block = buttonBlock.get();
            if (block instanceof ButtonBlock) {
                buttonBlock(block, baseName, alternateBaseName);
                itemModels().buttonInventory(buttonName, getTextureLocation(baseName, alternateBaseName));
            } else {
                VMinus.LOGGER.warn("Button block is not a ButtonBlock: {}", buttonName);
            }
        } else {
            VMinus.LOGGER.warn("Button block not found: {}", buttonName);
        }
    }

    private void registerTrapdoorBlock(String baseName) {
        String trapdoorName = BlockSet.correctBaseName(baseName) + "_trapdoor";
        ResourceLocation key = new ResourceLocation(modId, trapdoorName);
        RegistryObject<Block> trapdoorBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);
        Block block = trapdoorBlock.get();
        ResourceLocation trapdoorTexture = new ResourceLocation(modId, ":block/" + trapdoorName);

        trapdoorBlockWithRenderType((TrapDoorBlock) block, trapdoorTexture, true, "cutout");
        itemModels().trapdoorBottom(trapdoorName, trapdoorTexture);
    }

    private void registerDoorBlock(String baseName) {
        String doorName = BlockSet.correctBaseName(baseName) + "_door";
        ResourceLocation key = new ResourceLocation(modId, doorName);
        RegistryObject<Block> doorBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (doorBlock.isPresent()) {
            Block block = doorBlock.get();
            if (block instanceof DoorBlock) {
                doorBlock(block, doorName);
                itemModels().basicItem(new ResourceLocation(modId, doorName));
            } else {
                VMinus.LOGGER.warn("Door block is not a DoorBlock: {}", doorName);
            }
        } else {
            VMinus.LOGGER.warn("Door block not found: {}", doorName);
        }
    }

    private void registerSignBlock(StandingSignBlock standingSignBlock, WallSignBlock wallSignBlock, String baseName, Block block) {
        String standingSignName = BlockSet.correctBaseName(baseName) + "_sign";
        //String wallSignName = BlockSet.correctBaseName(baseName) + "_wall_sign";

        signBlock(standingSignBlock, wallSignBlock, blockTexture(block));
        itemModels().basicItem(new ResourceLocation(modId, standingSignName));
    }


    private void registerHangingSignBlock(Block signBlock, Block wallSignBlock, String baseName) {
        String correctBaseName = BlockSet.correctBaseName(baseName);
        String hangingSignName = correctBaseName + "_hanging_sign";
        ModelFile sign = models().sign(name(signBlock), new ResourceLocation(modId, "entity/signs/hanging/" + correctBaseName));
        hangingSignBlock(signBlock, wallSignBlock, sign);

        itemModels().basicItem(new ResourceLocation(modId, hangingSignName));
    }

    private void registerLeaves(Block block) {
        simpleBlockWithItem(block,
                models().singleTexture(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(block)));
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private void registerCrackedBlock(String baseName) {
        String crackedName = "cracked_" + baseName;
        ResourceLocation key = new ResourceLocation(modId, crackedName);
        RegistryObject<Block> crackedBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);

        if (crackedBlock.isPresent()) {
            simpleBlockWithItem(crackedBlock.get(), cubeAll(crackedBlock.get()));
        } else {
            VMinus.LOGGER.warn("Cracked block not found: {}", crackedName);
        }
    }

    private void wallBlock(Block block, String baseName, @Nullable String alternateBaseName) {
        wallBlock((WallBlock) block, getTextureLocation(baseName, alternateBaseName));
    }

    private void fenceBlock(Block block, String baseName, @Nullable String alternateBaseName) {
        fenceBlock((FenceBlock) block, baseName, getTextureLocation(baseName, alternateBaseName));
    }

    private void fenceGateBlock(Block block, String baseName, @Nullable String alternateBaseName) {
        fenceGateBlock((FenceGateBlock) block, baseName, getTextureLocation(baseName, alternateBaseName));
    }

    private void buttonBlock(Block block, String baseName, @Nullable String alternateBaseName) {
        buttonBlock((ButtonBlock) block, getTextureLocation(baseName, alternateBaseName));
    }

    private void pressurePlateBlock(Block block, String baseName, @Nullable String alternateBaseName) {
        pressurePlateBlock((PressurePlateBlock) block, getTextureLocation(baseName, alternateBaseName));
    }

    private void doorBlock(Block block, String doorName) {
        ResourceLocation bottomLocation = new ResourceLocation(modId, "block/" + doorName + "_bottom");
        ResourceLocation topLocation = new ResourceLocation(modId, "block/" + doorName + "_top");
        doorBlockWithRenderType((DoorBlock) block, bottomLocation, topLocation, "cutout_mipped");
    }

    private void slabBlock(String baseName, BlockSet.RenderType renderType, @Nullable String alternateBaseName) {
        String slabName = BlockSet.correctBaseName(baseName) + "_slab";
        ResourceLocation key = new ResourceLocation(modId, slabName);
        RegistryObject<Block> slabBlock = RegistryObject.create(key, ForgeRegistries.BLOCKS);


        if (slabBlock.isPresent()) {
            Block blockInstance = slabBlock.get();

            if (blockInstance instanceof SlabBlock) {
                if (!renderType.equals(BlockSet.RenderType.NORMAL)) {
                    slabBlockWithRenderType((SlabBlock) blockInstance, getTextureLocation(baseName, alternateBaseName), renderType);
                } else {
                    slabBlock((SlabBlock) blockInstance, getTextureLocation(baseName, alternateBaseName), getTextureLocation(baseName, alternateBaseName));
                }
            } else {
                VMinus.LOGGER.warn("Slab block is not a SlabBlock: {}", slabName);
            }
        } else {
            VMinus.LOGGER.warn("Slab block not found: {}", slabName);
        }
    }

    private void slabBlockWithRenderType(SlabBlock block, ResourceLocation texture, BlockSet.RenderType renderType) {
        String renderTypeString = BlockSet.renderTypeToString(renderType);
        getVariantBuilder(block)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(models().slab(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath(), texture, texture, texture)
                        .renderType(renderTypeString)))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(models().slabTop(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_top", texture, texture, texture)
                        .renderType(renderTypeString)))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(models().cubeAll(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath() + "_double", texture).renderType(renderTypeString)));
    }

    private void stairsBlock(Block block, String baseName, BlockSet.RenderType renderType, @Nullable String alternateBaseName) {
        stairsBlockWithRenderType((StairBlock) block, getTextureLocation(baseName, alternateBaseName), BlockSet.renderTypeToString(renderType));
    }

    public void simpleBlockWithItem(Block block, ModelFile model) {
        simpleBlock(block, model);
        itemModels().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())
                .parent(model);
    }
}
