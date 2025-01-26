package net.lixir.vminus.mixins;

import com.google.gson.JsonObject;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.ItemTabData;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public abstract class CreativeTabModeMixin {
    @Unique
    private final CreativeModeTab vminus$creativeModeTab = (CreativeModeTab) (Object) this;
    @Unique
    private HashMap<Item, Item> WAITING_LIST = new HashMap<>();
    @Unique
    private HashMap<Item, Boolean> WAITING_LIST_ORDER = new HashMap<>();
    @Unique
    private HashMap<Item, String> WAITING_LIST_TAB_ID = new HashMap<>();

    @Inject(method = "getIconItem", at = @At(value = "RETURN"), cancellable = true)
    public void getIconItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack != null && !itemStack.isEmpty()) {
            itemStack.getOrCreateTag().putBoolean("tab_item", true);
            cir.setReturnValue(itemStack);
        }
    }

    @Inject(method = "buildContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V"))
    private void vminus$buildContents(CreativeModeTab.ItemDisplayParameters displayContext, CallbackInfo ci) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        List<ItemStack> itemsToRemove = new ArrayList<>();
        for (ItemStack itemStack : accessor.getDisplayItems()) {
            JsonObject visionData = Vision.getData(itemStack);
            String hiddenTab = VisionProperties.getString(visionData, VisionProperties.Names.HIDDEN_TAB, itemStack);
            if (hiddenTab != null && !hiddenTab.isEmpty()) {
                if (BuiltInRegistries.CREATIVE_MODE_TAB.get(new ResourceLocation(hiddenTab)).equals(vminus$creativeModeTab)) {
                    itemsToRemove.add(itemStack);
                }
            } else if (VisionProperties.isHiddenInCreative(itemStack, visionData) || VisionProperties.isUnalteredHidden(itemStack.getItem(), visionData)) {
                itemsToRemove.add(itemStack);
            }
        }

        accessor.getDisplayItems().removeAll(itemsToRemove);
        itemsToRemove.clear();
        for (ItemStack itemStack : accessor.getSearchItems()) {
            JsonObject visionData = Vision.getData(itemStack);

            String hiddenTab = VisionProperties.getString(visionData, VisionProperties.Names.HIDDEN_TAB, itemStack);
            if (hiddenTab != null && !hiddenTab.isEmpty()) {
                if (BuiltInRegistries.CREATIVE_MODE_TAB.get(new ResourceLocation(hiddenTab)).equals(vminus$creativeModeTab)) {
                    itemsToRemove.add(itemStack);
                }
            } else if (VisionProperties.isHiddenInCreative(itemStack, visionData) || VisionProperties.isUnalteredHidden(itemStack.getItem(), visionData)) {
                itemsToRemove.add(itemStack);
            }
        }
        itemsToRemove.forEach(accessor.getSearchItems()::remove);

        if (!Vision.ITEM_TAB_DATA.isEmpty()) {
            for (ItemTabData data : Vision.ITEM_TAB_DATA) {
                Item targetItem = data.matchItem();
                Item item = data.item();
                vminus$addItemsToTab(targetItem, item, data.before(), data.tabId());
            }


            Map<Item, Item> waitingListCopy = new HashMap<>(WAITING_LIST);
            for (int i = 0; i < 100; i++) {
                for (Map.Entry<Item, Item> entry : waitingListCopy.entrySet()) {
                    Item item = entry.getKey();
                    Item targetItem = entry.getValue();
                    if (WAITING_LIST_ORDER.containsKey(item) && WAITING_LIST_TAB_ID.containsKey(item)) {
                        boolean before = WAITING_LIST_ORDER.get(item);
                        String tabId = WAITING_LIST_TAB_ID.get(item);
                        if (accessor.getDisplayItems().contains(new ItemStack(targetItem))) {
                            vminus$addItemsToTab(targetItem, item, before, tabId);
                            WAITING_LIST.remove(item);
                            WAITING_LIST_ORDER.remove(item);
                            WAITING_LIST_TAB_ID.remove(item);
                        }
                    }
                }
            }
        }
        WAITING_LIST.clear();
        WAITING_LIST_TAB_ID.clear();
        WAITING_LIST_ORDER.clear();

    }


    @Unique
    private void vminus$addItemsToTab(@Nullable Item targetItem, Item item, boolean before, @Nullable String tabId) {
        if (tabId == null)
            return;
        CreativeModeTab tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(new ResourceLocation(tabId));
        if (tab != null && !vminus$creativeModeTab.equals(tab))
            return;
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        Collection<ItemStack> items = accessor.getDisplayItems();
        Set<ItemStack> searchTabItems = accessor.getSearchItems();

        List<ItemStack> itemList = new ArrayList<>(items);
        if (itemList.isEmpty())
            return;

        int targetIndex = vminus$findItemIndex(itemList, targetItem);

        ItemStack newItemStack = new ItemStack(item);
        if (vminus$containsItem(itemList, item))
            itemList.remove(newItemStack);
        if (vminus$containsItem(searchTabItems, item))
            searchTabItems.remove(newItemStack);

        if (targetIndex != -1 && targetItem != null) {
            itemList.add(before ? targetIndex : targetIndex + 1, newItemStack);
        } else {
            WAITING_LIST.put(item, targetItem);
            WAITING_LIST_ORDER.put(item, before);
            WAITING_LIST_TAB_ID.put(item, tabId);
            return;
        }

        items.clear();
        items.addAll(itemList);

        LinkedHashSet<ItemStack> newSearchTabItems = new LinkedHashSet<>(searchTabItems);
        if (!vminus$containsItem(newSearchTabItems, item)) {
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