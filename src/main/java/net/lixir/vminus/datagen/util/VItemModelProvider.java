package net.lixir.vminus.datagen.util;

import net.lixir.vminus.registry.ItemModel;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
public class VItemModelProvider extends ItemModelProvider {
    final private String modId;

    public VItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId) {
        super(output, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    protected void registerModels() {
        for (Item item : UnifiedRegistry.fromId(modId).getItems()) {
            ItemEntryAccessor accessor = (ItemEntryAccessor) item;
            ItemEntry itemEntry = accessor.vminus$getEntry();
            if (itemEntry == null)
                continue;
            ItemModel model = itemEntry.getModel();
            if (model == null || model.equals(ItemModel.UNSET))
                continue;
            ItemModel.Data itemModelData = new ItemModel.Data(item, itemEntry);
            model.apply(itemModelData, this);
        }
    }

    public void fromBlockParent(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(blockItem);

            if (blockId == null || itemId == null) {
                throw new IllegalStateException("Unregistered block or item: " + blockItem);
            }

            withExistingParent(itemId.getPath(), modLoc("block/" + blockId.getPath()));
        } else {
            throw new IllegalArgumentException("Item should be a BlockItem type for From Block Parent model");
        }
    }

    public void handheld(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        withExistingParent(id.getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", new ResourceLocation(modId, "item/" + id.getPath()));
    }

    public void basic(Item item, ItemEntry itemEntry) {
        ResourceLocation id =  BuiltInRegistries.ITEM.getKey(item);
        String path;
        if (itemEntry.isFromBlock()) {
            path = "block/" + id.getPath();
        } else {
            path = "item/" + id.getPath();
        }
        withExistingParent(id.getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(modId, path));
    }

    public void doublePane(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            String blockPath = BuiltInRegistries.BLOCK.getKey(block).getPath();
            if (blockPath.endsWith("_pane"))
                blockPath = blockPath.substring(0, blockPath.indexOf("_pane"));
            withExistingParent(BuiltInRegistries.ITEM.getKey(blockItem).getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(modId, "block/" + blockPath + "_top"));
        } else {
            throw new IllegalArgumentException("Item should be a BlockItem type for Double Pane ItemModel");
        }
    }

    public void pane(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            String blockPath = BuiltInRegistries.BLOCK.getKey(block).getPath();
            if (blockPath.endsWith("_pane"))
                blockPath = blockPath.substring(0, blockPath.indexOf("_pane"));
            withExistingParent(BuiltInRegistries.ITEM.getKey(blockItem).getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(modId, "block/" + blockPath));
        } else {
            throw new IllegalArgumentException("Item should be a BlockItem type for Pane ItemModel");
        }
    }
}
