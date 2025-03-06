package net.lixir.vminus.util.setup.item;

import net.lixir.vminus.util.setup.SetupRecipe;
import net.lixir.vminus.util.setup.SetupTag;
import net.lixir.vminus.util.setup.SetupTint;
import net.minecraftforge.common.IExtensibleEnum;

public enum ItemSetupType {
    SIMPLE(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    SWORD(ItemSetupModel.HANDHELD, SetupRecipe.NONE, SetupTag.SWORD, SetupTint.NONE),
    PICKAXE(ItemSetupModel.HANDHELD, SetupRecipe.NONE, SetupTag.PICKAXE, SetupTint.NONE),
    AXE(ItemSetupModel.HANDHELD, SetupRecipe.NONE, SetupTag.AXE, SetupTint.NONE),
    SHOVEL(ItemSetupModel.HANDHELD, SetupRecipe.NONE, SetupTag.SHOVEL, SetupTint.NONE),
    HOE(ItemSetupModel.HANDHELD, SetupRecipe.NONE, SetupTag.HOE, SetupTint.NONE),
    TIERED_HELMET(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    TIERED_CHESTPLATE(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    TIERED_LEGGINGS(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    TIERED_BOOTS(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    UNTIERED_HELMET(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    UNTIERED_CHESTPLATE(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    UNTIERED_LEGGINGS(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),
    UNTIERED_BOOTS(ItemSetupModel.SIMPLE, SetupRecipe.NONE, SetupTag.NONE, SetupTint.NONE),

    ;

    private final ItemSetupModel itemSetupModel;
    private final SetupRecipe setupRecipe;
    private final SetupTag setupTag;
    private final SetupTint setupTint;

    ItemSetupType(ItemSetupModel itemSetupModel, SetupRecipe setupRecipe, SetupTag setupTag, SetupTint setupTint) {
        this.itemSetupModel = itemSetupModel;
        this.setupRecipe = setupRecipe;
        this.setupTag = setupTag;
        this.setupTint = setupTint;
    }

    public ItemSetupModel getItemSetupModel() {
        return itemSetupModel;
    }

    public SetupRecipe getSetupRecipe() {
        return setupRecipe;
    }

    public SetupTag getSetupTag() {
        return setupTag;
    }

    public SetupTint getSetupTint() {
        return setupTint;
    }
}
