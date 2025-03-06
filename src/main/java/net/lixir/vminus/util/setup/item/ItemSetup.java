package net.lixir.vminus.util.setup.item;

import net.lixir.vminus.util.setup.SetupRecipe;
import net.lixir.vminus.util.setup.SetupTag;
import net.lixir.vminus.util.setup.SetupTint;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ItemSetup {
    private final RegistryObject<Item> itemRegistryObject;
    private final ItemSetupModel itemSetupModel;
    private final SetupTag setupTag;
    private final SetupRecipe setupRecipe;
    private final SetupTint setupTint;

    private ItemSetup(Builder builder) {
      this.itemRegistryObject = builder.itemRegistryObject;
      this.itemSetupModel = builder.itemSetupModel;
      this.setupTag = builder.setupTag;
      this.setupRecipe = builder.setupRecipe;
      this.setupTint = builder.setupTint;
    }

    public RegistryObject<Item> getItemRegistryObject() {
        return itemRegistryObject;
    }

    public ItemSetupModel getItemSetupModel() {
        return itemSetupModel;
    }

    public SetupTag getSetupTag() {
        return setupTag;
    }

    public SetupRecipe getSetupRecipe() {
        return setupRecipe;
    }

    public SetupTint getSetupTint() {
        return setupTint;
    }


    public static class Builder {
        private RegistryObject<Item> itemRegistryObject;

        private ItemSetupModel itemSetupModel;
        private SetupRecipe setupRecipe;
        private SetupTag setupTag;
        private SetupTint setupTint;

        public Builder(RegistryObject<Item> itemRegistryObject, ItemSetupType itemSetupType) {
            this.itemRegistryObject = itemRegistryObject;
            this.itemSetupModel = itemSetupType.getItemSetupModel();
            this.setupRecipe = itemSetupType.getSetupRecipe();
            this.setupTag = itemSetupType.getSetupTag();
            this.setupTint = itemSetupType.getSetupTint();
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
            this.setupRecipe = SetupRecipe.NONE;
            return this;
        }

        public Builder tint(SetupTint setupTint) {
            this.setupTint = setupTint;
            return this;
        }

        public Builder model(ItemSetupModel itemSetupModel) {
            this.itemSetupModel = itemSetupModel;
            return this;
        }


        public ItemSetup build() {
            return new ItemSetup(this);
        }
    }
}
