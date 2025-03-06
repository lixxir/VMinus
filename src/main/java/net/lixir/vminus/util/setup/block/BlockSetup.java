package net.lixir.vminus.util.setup.block;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.lixir.vminus.util.setup.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class BlockSetup {
    private final BlockItemRegistryPair blockItemPair;
    private final BlockSetupModel blockSetupModel;
    private final SetupRecipe setupRecipe;
    private final BlockSetupLootTable blockSetupLootTable;
    private final SetupTag setupTag;
    private final Block baseBlock;
    private final RegistryObject<Block> baseBlockRegistry;
    private final SetupToolType setupToolType;
    private final RenderType renderType;
    private final SetupTint setupTint;

    private BlockSetup(Builder builder) {
        this.blockItemPair = builder.blockItemPair;
        this.blockSetupModel = builder.datagenSetupModel;
        this.setupRecipe = builder.blockSetupRecipe;
        this.blockSetupLootTable = builder.blockSetupLootTable;
        this.setupTag = builder.setupTag;
        this.setupToolType = builder.setupToolType;
        this.baseBlock = builder.baseBlock;
        this.renderType = builder.renderType;
        this.setupTint = builder.setupTint;
        this.baseBlockRegistry = builder.baseBlockRegistry;
    }

    public BlockItemRegistryPair getBlockItemPair() {
        return blockItemPair;
    }

    public BlockSetupModel getSetupModel() {
        return blockSetupModel;
    }

    public SetupRecipe getSetupRecipe() {
        return setupRecipe;
    }

    @Nullable
    public Block getBaseBlock() {
        return baseBlock != null ? baseBlock : (baseBlockRegistry != null ? baseBlockRegistry.get() : null);
    }

    public BlockSetupLootTable getDatagenLootTable() {
        return blockSetupLootTable;
    }

    public SetupTag getDatagenTag() {
        return setupTag;
    }

    public SetupToolType getDatagenToolType() {
        return setupToolType;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public SetupTint getDatagenTint() {
        return setupTint;
    }

    public static class Builder {
        private BlockItemRegistryPair blockItemPair;
        private BlockSetupModel datagenSetupModel;
        private SetupRecipe blockSetupRecipe;
        private BlockSetupLootTable blockSetupLootTable;
        private SetupTag setupTag;
        private RenderType renderType;
        private SetupTint setupTint;

        private Block baseBlock = null;
        private RegistryObject<Block> baseBlockRegistry = null;
        private SetupToolType setupToolType = null;

        public Builder(RegistryObject<Block> blockRegistryObject, BlockSetupType blockSetupType) {
            this.blockItemPair = new BlockItemRegistryPair(blockRegistryObject, null);
            this.datagenSetupModel = blockSetupType.getDatagenBlockModel();
            this.blockSetupRecipe = blockSetupType.getDatagenRecipe();
            this.blockSetupLootTable = blockSetupType.getDatagenLootTable();
            this.setupTag = blockSetupType.getDatagenTag();
            this.renderType = blockSetupType.getRenderType();
            this.setupTint = blockSetupType.getDatagenTint();
        }

        public Builder(RegistryObject<Block> blockRegistryObject, RegistryObject<Item> itemRegistryObject, BlockSetupType blockSetupType) {
            this.blockItemPair = new BlockItemRegistryPair(blockRegistryObject, itemRegistryObject);
            this.datagenSetupModel = blockSetupType.getDatagenBlockModel();
            this.blockSetupRecipe = blockSetupType.getDatagenRecipe();
            this.blockSetupLootTable = blockSetupType.getDatagenLootTable();
            this.setupTag = blockSetupType.getDatagenTag();
            this.renderType = blockSetupType.getRenderType();
            this.setupTint = blockSetupType.getDatagenTint();
        }

        public Builder(RegistryObject<Block> blockRegistryObject) {
            this.blockItemPair = new BlockItemRegistryPair(blockRegistryObject, null);
        }

        public Builder(RegistryObject<Block> blockRegistryObject, RegistryObject<Item> itemRegistryObject) {
            this.blockItemPair = new BlockItemRegistryPair(blockRegistryObject, itemRegistryObject);
        }

        public Builder(BlockItemRegistryPair blockItemPair) {
            this.blockItemPair = blockItemPair;
        }

        public Builder(BlockItemRegistryPair blockItemPair, BlockSetupType blockSetupType) {
            this.blockItemPair = blockItemPair;
            this.datagenSetupModel = blockSetupType.getDatagenBlockModel();
            this.blockSetupRecipe = blockSetupType.getDatagenRecipe();
            this.blockSetupLootTable = blockSetupType.getDatagenLootTable();
            this.setupTag = blockSetupType.getDatagenTag();
            this.renderType = blockSetupType.getRenderType();
            this.setupTint = blockSetupType.getDatagenTint();
        }

        public Builder noLootTable() {
            this.blockSetupLootTable = BlockSetupLootTable.NONE;
            return this;
        }

        public Builder noTag() {
            this.setupTag = SetupTag.NONE;
            return this;
        }

        public Builder noTint() {
            this.setupTint = SetupTint.NONE;
            return this;
        }

        public Builder noRecipe() {
            this.blockSetupRecipe = SetupRecipe.NONE;
            return this;
        }

        public Builder tint(SetupTint setupTint) {
            this.setupTint = setupTint;
            return this;
        }

        public Builder model(BlockSetupModel datagenSetupModel) {
            this.datagenSetupModel = datagenSetupModel;
            return this;
        }

        public Builder renderType(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public Builder baseBlock(Block block) {
            this.baseBlock = block;
            this.baseBlockRegistry = null;
            return this;
        }

        public Builder baseBlock(RegistryObject<Block> blockRegistry) {
            this.baseBlockRegistry = blockRegistry;
            this.baseBlock = null;
            return this;
        }

        public Builder toolType(SetupToolType setupToolType) {
            this.setupToolType = setupToolType;
            return this;
        }

        public BlockSetup build() {
            return new BlockSetup(this);
        }
    }
}
