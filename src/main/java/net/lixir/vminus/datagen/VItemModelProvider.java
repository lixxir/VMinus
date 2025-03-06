package net.lixir.vminus.datagen;

import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.item.ItemSetup;
import net.lixir.vminus.util.setup.item.ItemSetupModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class VItemModelProvider extends ItemModelProvider {
    final private String modId;

    public VItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId) {
        super(output, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        for (ItemSetup itemSetup : SetupRegistries.ITEMS.getValues(modId)) {
            RegistryObject<Item> itemRegistryObject = itemSetup.getItemRegistryObject();

            ItemSetupModel itemSetupModel = itemSetup.getItemSetupModel();
            if (itemRegistryObject != null && itemSetupModel != null && !itemSetupModel.equals(ItemSetupModel.NONE)) {
                switch (itemSetupModel) {
                    case SIMPLE -> simpleItem(itemRegistryObject);
                    case HANDHELD -> handheldItem(itemRegistryObject);
                }
            }
        }
    }


    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(modId, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(modId, "item/" + item.getId().getPath()));
    }
}
