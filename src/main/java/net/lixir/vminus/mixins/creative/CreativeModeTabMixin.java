package net.lixir.vminus.mixins.creative;

import net.lixir.vminus.vision.VisionDuck;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public class CreativeModeTabMixin implements VisionDuck {
    @Unique
    private final CreativeModeTab vminus$creativeModeTab = (CreativeModeTab) (Object) this;
    @Unique
    private HashMap<Item, Item> WAITING_LIST = new HashMap<>();
    @Unique
    private HashMap<Item, Boolean> WAITING_LIST_ORDER = new HashMap<>();

    @Unique
    private int vMinus$visionIndex = 0;

    /*
    @Inject(method = "getIconItem", at = @At(value = "RETURN"), cancellable = true)
    public void getIconItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack;
        ItemStack iconItemStack = vMinus$getVision().icon.value();
        if (iconItemStack != null) {
            itemStack = iconItemStack;
        } else {
            itemStack = cir.getReturnValue();
        }
        itemStack.getOrCreateTag().putBoolean("tab_item", true);
        cir.setReturnValue(itemStack);
    }

    @Inject(method = "buildContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V"))
    private void vminus$buildContents(CreativeModeTab.ItemDisplayParameters displayContext, CallbackInfo ci) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        List<Item> itemsToRemove = new ArrayList<>(vMinus$getVision().remove.values().stream()
                .filter((VisionItemReplacement t) -> t.itemStack() != null)
                .map((VisionItemReplacement t) -> t.itemStack().getItem())
                .toList());

        List<TagKey<Item>> itemTagsToRemove = new ArrayList<>(vMinus$getVision().remove.values().stream()
                .map(VisionItemReplacement::tag)
                .filter(Objects::nonNull)
                .toList());


        vminus$processHiddenItems(accessor.getDisplayItems(), itemsToRemove, itemTagsToRemove);
        vminus$processHiddenItems(accessor.getSearchItems(), itemsToRemove, itemTagsToRemove);


        List<VisionCreativeOrder> orders = new ArrayList<>(vMinus$getVision().order.values());
        List<ItemStack> itemList = new ArrayList<>(accessor.getDisplayItems());

        orders.sort(Comparator.comparingInt(order -> {
            ItemStack targetStack = order.getTargetItemStack();
            return targetStack != null ? vminus$findItemIndex(itemList, targetStack.getItem()) : Integer.MAX_VALUE;
        }));


        for (VisionCreativeOrder order : orders) {
            ItemStack itemStack = order.getItemStack();
            if (itemStack == null)
                continue;

            Item item = itemStack.getItem();
            ItemStack targetItemStack = order.getTargetItemStack();

            if (targetItemStack != null) {
                Item targetItem = targetItemStack.getItem();
                vminus$addItemsToTab(targetItem, item, order.isBefore());
            } else {
                if (!accessor.getDisplayItems().contains(itemStack))
                    accessor.getDisplayItems().add(itemStack);
                if (!accessor.getSearchItems().contains(itemStack))
                    accessor.getSearchItems().add(itemStack);
            }
        }

        boolean updated;
        do {
            updated = false;
            Iterator<Map.Entry<Item, Item>> iterator = WAITING_LIST.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Item, Item> entry = iterator.next();
                Item item = entry.getKey();
                Item targetItem = entry.getValue();
                boolean before = WAITING_LIST_ORDER.getOrDefault(item, false);

                if (accessor.getDisplayItems().contains(new ItemStack(targetItem))) {
                    vminus$addItemsToTab(targetItem, item, before);
                    iterator.remove();
                    WAITING_LIST_ORDER.remove(item);
                    updated = true;
                }
            }
        } while (updated);


        WAITING_LIST.clear();
        WAITING_LIST_ORDER.clear();
        vminus$creativeModeTab.rebuildSearchTree();
    }

    @Unique
    private void vminus$processHiddenItems(Collection<ItemStack> itemStacks, List<Item> itemsToRemove, List<TagKey<Item>> itemTagsToRemove) {
        var tagCollection = ForgeRegistries.ITEMS.tags();


        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null)
                continue;

            Item item = itemStack.getItem();
            ItemVision itemVision = ItemVision.of(itemStack);

            Boolean banned = itemVision.ban.value(new VisionContext(itemStack));
            VisionItemReplacement visionItemReplacement = itemVision.replace.value(new VisionContext(itemStack));
            boolean isTaggedForRemoval = itemTagsToRemove.stream().anyMatch(tagKey ->
                    tagCollection != null && tagCollection.getTag(tagKey).contains(item));

            if ((banned != null && banned) ||
                    (visionItemReplacement != null &&
                            (visionItemReplacement.itemStack() != null || visionItemReplacement.tag() != null)) ||
                    itemsToRemove.contains(item) || isTaggedForRemoval) {
                itemsToRemove.add(item);
            }
        }
        itemStacks.removeIf(itemStack -> itemsToRemove.contains(itemStack.getItem()) ||
                (tagCollection != null && itemTagsToRemove.stream().anyMatch(tagKey ->
                        tagCollection.getTag(tagKey).contains(itemStack.getItem())))
        );
    }




    @Unique
    private void vminus$addItemsToTab(@Nullable Item targetItem, Item item, boolean before) {
        Boolean banned = ItemVision.of(item).ban.value(new VisionContext(item));
        if (banned != null && banned)
            return;
        Boolean targetBanned = ItemVision.of(targetItem).ban.value(new VisionContext(targetItem));
        if (targetBanned != null && targetBanned)
            return;

        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vminus$creativeModeTab;
        Collection<ItemStack> items = accessor.getDisplayItems();
        Set<ItemStack> searchTabItems = accessor.getSearchItems();

        List<ItemStack> itemList = new ArrayList<>(items);
        if (itemList.isEmpty())
            return;

        int targetIndex = vminus$findItemIndex(itemList, targetItem);

        ItemStack newItemStack = item.getDefaultInstance();
        if (newItemStack.isEmpty())
            return;
        if (vminus$containsItem(itemList, item))
            itemList.remove(newItemStack);
        if (vminus$containsItem(searchTabItems, item))
            searchTabItems.remove(newItemStack);

        if (targetIndex != -1 && targetItem != null) {
            itemList.add(before ? targetIndex : targetIndex + 1, newItemStack);
        } else {
            WAITING_LIST.put(item, targetItem);
            WAITING_LIST_ORDER.put(item, before);
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

     */

    @Override
    public void vMinus$setVisionIndex(int index) {
        vMinus$visionIndex = index;
    }

    @Override
    public int vMinus$getVisionIndex() {
        return vMinus$visionIndex;
    }
}