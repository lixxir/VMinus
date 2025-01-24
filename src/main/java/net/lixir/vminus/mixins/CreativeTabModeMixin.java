package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.BlockSet;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionValueHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public abstract class CreativeTabModeMixin {
    @Unique
    private final CreativeModeTab vminus$creativeModeTab = (CreativeModeTab) (Object) this;

    @Unique
    private final Map<Item, String> vminus$matchTabItemCache = new HashMap<>();

    @Unique
    private final Map<Item, String> vminus$matchItemCache = new HashMap<>();

    @Unique
    private final Map<Item, Integer> vminus$offsetCache = new HashMap<>();


    @Inject(method = "getIconItem", at = @At(value = "RETURN"), cancellable = true)
    public void getIconItem(CallbackInfoReturnable<ItemStack> cir) {
       ItemStack itemStack = cir.getReturnValue();
       CompoundTag tag = itemStack.getTag();
       if (tag != null)
            tag.putBoolean("tab_item", true);
       cir.setReturnValue(itemStack);
    }


    @Inject(method = "buildContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V"))
    private void CreativeTabOverride(CreativeModeTab.ItemDisplayParameters displayContext, CallbackInfo ci) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;

        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            Item item = entry.getValue();

            JsonObject visionData = Vision.getData(item);

            if (VisionProperties.isHiddenInCreative(item, visionData)
                    && !VisionProperties.isUnalteredHidden(item,visionData)) {
                continue;
            }

            if (visionData != null && visionData.has("creative_order")) {
                String matchTabItemId = vminus$matchTabItemCache.computeIfAbsent(item, i ->
                        VisionValueHandler.getFirstValidString(visionData, "creative_order", i, "match_tab_item"));

                if (matchTabItemId == null || matchTabItemId.isEmpty()) {
                    VMinus.LOGGER.warn("{} has an incorrectly defined or empty match_tab_item property in a vision, skipping.", item);
                    continue;
                }

                if (!vminus$tabIconMatches(ForgeRegistries.ITEMS.getValue(new ResourceLocation(matchTabItemId)))) {
                    continue;
                }

                String matchItemId = vminus$matchItemCache.computeIfAbsent(item, i ->
                        VisionValueHandler.getFirstValidString(visionData, "creative_order", i, "match_item"));

                if (matchItemId == null || matchItemId.isEmpty()) {
                    VMinus.LOGGER.warn("{} has an incorrectly defined or empty creative_order property in a vision, skipping.", item);
                    continue;
                }

                Integer offsetInt = vminus$offsetCache.computeIfAbsent(item, i -> {
                    Number offset = VisionValueHandler.getFirstValidNumber(visionData, "creative_order", i, "offset");
                    return offset != null ? offset.intValue() : null;
                });

                if (offsetInt == null) {
                    VMinus.LOGGER.warn("{} has an incorrectly defined or empty offset property in a vision, skipping.", item);
                    continue;
                }

                vminus$addItemsToTab(ForgeRegistries.ITEMS.getValue(new ResourceLocation(matchItemId)), item, offsetInt);
            }
        }

        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            Item matchItem = ForgeRegistries.ITEMS.getValue(blockSet.getTabItem());
            if (blockSet.getAfterCreativeTabItem() != null) {
                Item targetItem = ForgeRegistries.ITEMS.getValue(blockSet.getAfterCreativeTabItem());
                int offset = blockSet.getOffset();
                Item baseBlock = blockSet.getBaseBlock().asItem();
                if (vminus$tabIconMatches(matchItem)) {
                    if (blockSet.hasButton()) {
                        Item buttonBlock = blockSet.getButtonBlock().asItem();
                        vminus$addItemsToTab(targetItem, buttonBlock, offset);
                    }

                    if (blockSet.hasPressurePlate()) {
                        Item pressurePlateBlock = blockSet.getPressurePlateBlock().asItem();
                        vminus$addItemsToTab(targetItem, pressurePlateBlock, offset);
                    }

                    if (blockSet.hasTrapdoor()) {
                        Item trapdoorBlock = blockSet.getTrapdoorBlock().asItem();
                        vminus$addItemsToTab(targetItem, trapdoorBlock, offset);
                    }

                    if (blockSet.hasDoor()) {
                        Item doorBlock = blockSet.getDoorBlock().asItem();
                        vminus$addItemsToTab(targetItem, doorBlock, offset);
                    }

                    if (blockSet.hasFenceGate()) {
                        Item fenceGateBlock = blockSet.getFenceGateBlock().asItem();
                        vminus$addItemsToTab(targetItem, fenceGateBlock, offset);
                    }

                    if (blockSet.hasFence()) {
                        Item fenceBlock = blockSet.getFenceBlock().asItem();
                        vminus$addItemsToTab(targetItem, fenceBlock, offset);
                    }

                    if (blockSet.hasWall()) {
                        Item wallBlock = blockSet.getWallBlock().asItem();
                        vminus$addItemsToTab(targetItem, wallBlock, offset);
                    }

                    if (blockSet.hasSlab()) {
                        Item slabBlock = blockSet.getSlabBlock().asItem();
                        vminus$addItemsToTab(targetItem, slabBlock, offset);
                    }

                    if (blockSet.hasStairs()) {
                        Item stairsBlock = blockSet.getStairsBlock().asItem();
                        vminus$addItemsToTab(targetItem, stairsBlock, offset);
                    }

                    if (blockSet.hasCracked()) {
                        Item crackedBlock = blockSet.getCrackedBlock().asItem();
                        vminus$addItemsToTab(targetItem, crackedBlock, offset);
                    }

                    vminus$addItemsToTab(targetItem, baseBlock, offset);

                    if (blockSet.hasLog()) {
                        Item strippedWoodBlock = blockSet.getStrippedWoodBlock().asItem();
                        vminus$addItemsToTab(targetItem, strippedWoodBlock, offset);
                        Item strippedLogBlock = blockSet.getStrippedLogBlock().asItem();
                        vminus$addItemsToTab(targetItem, strippedLogBlock, offset);
                        Item woodBlock = blockSet.getWoodBlock().asItem();
                        vminus$addItemsToTab(targetItem, woodBlock, offset);
                        Item logBlock = blockSet.getLogBlock().asItem();
                        vminus$addItemsToTab(targetItem, logBlock, offset);
                    }

                } else if (vminus$tabIconMatches(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:oak_sign")))) {
                    if (blockSet.hasHangingSign()) {
                        Item hangingSign = blockSet.getHangingSignBlock().asItem();
                        vminus$addItemsToTab(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:oak_sign")), hangingSign, offset);
                    }

                    if (blockSet.hasSign()) {
                        Item signBlock = blockSet.getStandingSignBlock().asItem();
                        vminus$addItemsToTab(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:oak_sign")), signBlock, offset);
                    }
                }
            } else {
                Item baseBlock = blockSet.getBaseBlock().asItem();

                if (blockSet.hasButton()) {
                    Item buttonBlock = blockSet.getButtonBlock().asItem();
                    vminus$addItemsToTab(buttonBlock);
                }

                if (blockSet.hasPressurePlate()) {
                    Item pressurePlateBlock = blockSet.getPressurePlateBlock().asItem();
                    vminus$addItemsToTab(pressurePlateBlock);
                }

                if (blockSet.hasFenceGate()) {
                    Item fenceGateBlock = blockSet.getFenceGateBlock().asItem();
                    vminus$addItemsToTab(fenceGateBlock);
                }

                if (blockSet.hasFence()) {
                    Item fenceBlock = blockSet.getFenceBlock().asItem();
                    vminus$addItemsToTab(fenceBlock);
                }

                if (blockSet.hasWall()) {
                    Item wallBlock = blockSet.getWallBlock().asItem();
                    vminus$addItemsToTab(wallBlock);
                }

                if (blockSet.hasSlab()) {
                    Item slabBlock = blockSet.getSlabBlock().asItem();
                    vminus$addItemsToTab(slabBlock);
                }

                if (blockSet.hasStairs()) {
                    Item stairsBlock = blockSet.getStairsBlock().asItem();
                    vminus$addItemsToTab(stairsBlock);
                }

                if (blockSet.hasCracked()) {
                    Item crackedBlock = blockSet.getCrackedBlock().asItem();
                    vminus$addItemsToTab(crackedBlock);
                }

                vminus$addItemsToTab(baseBlock);
            }

        }



        List<ItemStack> itemsToRemove = new ArrayList<>();
        for (ItemStack itemStack : accessor.getDisplayItems()) {
            JsonObject visionData = Vision.getData(itemStack);
            if (VisionProperties.isHiddenInCreative(itemStack,visionData) || VisionProperties.isUnalteredHidden(itemStack.getItem(),visionData)) {
                itemsToRemove.add(itemStack);
            }
        }

        accessor.getDisplayItems().removeAll(itemsToRemove);
        itemsToRemove.clear();
        for (ItemStack itemStack : accessor.getSearchItems()) {
            JsonObject visionData = Vision.getData(itemStack);
            if (VisionProperties.isHiddenInCreative(itemStack,visionData) || VisionProperties.isUnalteredHidden(itemStack.getItem(),visionData)) {
                itemsToRemove.add(itemStack);
            }
        }

        accessor.getSearchItems().removeAll(itemsToRemove);
    }




    @Unique
    private boolean vminus$tabIconMatches(Item item) {
        return vminus$creativeModeTab.getIconItem().getItem().equals(item);
    }

    @Unique
    private void vminus$addItemsToTab(Item item) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        ItemStack itemStack = new ItemStack(item, 1);
        accessor.getDisplayItems().add(itemStack);
        accessor.getSearchItems().add(itemStack);
    }

    @Unique
    private void vminus$addItemsToTab(Item targetItem, Item moddedItem, int offset) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        Collection<ItemStack> items = accessor.getDisplayItems();
        Set<ItemStack> searchTabItems = accessor.getSearchItems();

        List<ItemStack> itemList = new ArrayList<>(items);

        int targetIndex = vminus$findItemIndex(itemList, targetItem);
        if (targetIndex == -1) {
            return;
        }

        ItemStack newItemStack = new ItemStack(moddedItem);
        if (vminus$containsItem(itemList, moddedItem) || vminus$containsItem(searchTabItems, moddedItem)) {
            return;
        }

        int insertIndex = targetIndex + offset;
        insertIndex = Math.max(0, Math.min(insertIndex, itemList.size()));

        itemList.add(insertIndex, newItemStack);

        items.clear();
        items.addAll(itemList);

        LinkedHashSet<ItemStack> newSearchTabItems = new LinkedHashSet<>(searchTabItems);
        if (!vminus$containsItem(newSearchTabItems, moddedItem)) {
            newSearchTabItems.add(newItemStack);
        }

        searchTabItems.clear();
        searchTabItems.addAll(newSearchTabItems);
    }

    @Unique
    private int vminus$findItemIndex(List<ItemStack> items, Item targetItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem() == targetItem) {
                return i;
            }
        }
        return -1;
    }

    @Unique
    private boolean vminus$containsItem(Collection<ItemStack> items, Item item) {
        for (ItemStack stack : items) {
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

}
