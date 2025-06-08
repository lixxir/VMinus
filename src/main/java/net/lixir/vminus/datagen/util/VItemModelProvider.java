package net.lixir.vminus.datagen.util;

import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

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
            ItemEntry.Model model = itemEntry.getModel();
            if (model == null || model == ItemEntry.Model.UNSET)
                continue;
            switch (model) {
                case HANDHELD -> handheld(item);
                case PANE -> pane((BlockItem) item);
                case BASIC -> basic(item, itemEntry);
                case PARENT_BLOCK -> withBlockParent((BlockItem) item);
                case DOUBLE_PANE -> doublePane((BlockItem) item);
            }
        }
    }

    public void withBlockParent(BlockItem item) {
        Block block = item.getBlock();
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);

        if (blockId == null || itemId == null) {
            throw new IllegalStateException("Unregistered block or item: " + item);
        }

        withExistingParent(itemId.getPath(), modLoc("block/" + blockId.getPath()));
    }



    protected ItemModelBuilder handheld(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return withExistingParent(id.getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", new ResourceLocation(modId, "item/" + id.getPath()));
    }

    protected ItemModelBuilder basic(Item item, ItemEntry itemEntry) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        String path;
        if (itemEntry.isFromBlock()) {
            path = "block/" + id.getPath();
        } else {
            path = "item/" + id.getPath();
        }
        return withExistingParent(id.getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(modId, path));
    }

    protected ItemModelBuilder doublePane(BlockItem blockItem) {
        Block block = blockItem.getBlock();
        String blockPath = ForgeRegistries.BLOCKS.getKey(block).getPath();
        if (blockPath.endsWith("_pane"))
            blockPath = blockPath.substring(0, blockPath.indexOf("_pane"));
        return withExistingParent(ForgeRegistries.ITEMS.getKey(blockItem).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(modId, "block/" + blockPath + "_top"));
    }

    protected ItemModelBuilder pane(BlockItem blockItem) {
        Block block = blockItem.getBlock();
        String blockPath = ForgeRegistries.BLOCKS.getKey(block).getPath();
        if (blockPath.endsWith("_pane"))
            blockPath = blockPath.substring(0, blockPath.indexOf("_pane"));
        return withExistingParent(ForgeRegistries.ITEMS.getKey(blockItem).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(modId, "block/" + blockPath));
    }
}
