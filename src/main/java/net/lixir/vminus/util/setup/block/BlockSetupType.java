package net.lixir.vminus.util.setup.block;

import net.lixir.vminus.util.setup.SetupRecipe;
import net.lixir.vminus.util.setup.SetupTag;
import net.lixir.vminus.util.setup.SetupTint;
import net.lixir.vminus.util.setup.item.ItemSetupModel;
import net.lixir.vminus.util.setup.item.ItemSetupType;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.IExtensibleEnum;

public enum BlockSetupType {
    EMPTY(BlockSetupModel.NONE, SetupRecipe.NONE, BlockSetupLootTable.NONE, SetupTag.NONE, RenderType.solid(), SetupTint.NONE),
    SIMPLE(BlockSetupModel.SIMPLE, SetupRecipe.NONE, BlockSetupLootTable.DROP_SELF, SetupTag.NONE, RenderType.solid(), SetupTint.NONE),
    NYLIUM(BlockSetupModel.SIMPLE, SetupRecipe.NONE, BlockSetupLootTable.DROP_SELF, SetupTag.NONE, RenderType.solid(), SetupTint.NONE),
    STAIRS(BlockSetupModel.STAIRS, SetupRecipe.STAIRS, BlockSetupLootTable.DROP_SELF, SetupTag.STAIRS, RenderType.solid(), SetupTint.NONE),
    SLAB(BlockSetupModel.SLAB, SetupRecipe.SLAB, BlockSetupLootTable.SLAB, SetupTag.SLAB, RenderType.solid(), SetupTint.NONE),
    WALL(BlockSetupModel.WALL, SetupRecipe.WALL, BlockSetupLootTable.DROP_SELF, SetupTag.WALL, RenderType.solid(), SetupTint.NONE),
    UNTINTED_PLANT(BlockSetupModel.CROSS, SetupRecipe.NONE, BlockSetupLootTable.DROP_SELF, SetupTag.NONE, RenderType.cutout(), SetupTint.NONE),
    TINTED_PLANT(BlockSetupModel.TINTED_CROSS, SetupRecipe.NONE, BlockSetupLootTable.DROP_SELF, SetupTag.NONE, RenderType.cutout(), SetupTint.GRASS),
    UNTINTED_GRASS(BlockSetupModel.CROSS, SetupRecipe.NONE, BlockSetupLootTable.HOE_REQUIRED, SetupTag.NONE, RenderType.cutout(), SetupTint.NONE),
    TINTED_GRASS(BlockSetupModel.TINTED_CROSS, SetupRecipe.NONE, BlockSetupLootTable.HOE_REQUIRED, SetupTag.NONE, RenderType.cutout(), SetupTint.GRASS),
    UNTINTED_LEAVES(BlockSetupModel.LEAVES, SetupRecipe.NONE, BlockSetupLootTable.LEAVES, SetupTag.LEAVES, RenderType.cutout(), SetupTint.NONE),
    TINTED_LEAVES(BlockSetupModel.LEAVES, SetupRecipe.NONE, BlockSetupLootTable.LEAVES, SetupTag.LEAVES, RenderType.cutout(), SetupTint.FOLIAGE),

    ;


    private final BlockSetupModel datagenSetupModel;
    private final SetupRecipe blockSetupRecipe;
    private final BlockSetupLootTable blockSetupLootTable;
    private final SetupTag setupTag;
    private final RenderType renderType;
    private final SetupTint setupTint;

    BlockSetupType(BlockSetupModel datagenSetupModel, SetupRecipe blockSetupRecipe, BlockSetupLootTable blockSetupLootTable, SetupTag setupTag, RenderType renderType, SetupTint setupTint) {
        this.datagenSetupModel = datagenSetupModel;
        this.blockSetupRecipe = blockSetupRecipe;
        this.blockSetupLootTable = blockSetupLootTable;
        this.setupTag = setupTag;
        this.renderType = renderType;
        this.setupTint = setupTint;
    }

    public BlockSetupModel getDatagenBlockModel() {
        return datagenSetupModel;
    }

    public SetupRecipe getDatagenRecipe() {
        return blockSetupRecipe;
    }

    public BlockSetupLootTable getDatagenLootTable() {
        return blockSetupLootTable;
    }

    public SetupTag getDatagenTag() {
        return setupTag;
    }

    public RenderType getRenderType() {
        return renderType;
    }

    public SetupTint getDatagenTint() {
        return setupTint;
    }
}
