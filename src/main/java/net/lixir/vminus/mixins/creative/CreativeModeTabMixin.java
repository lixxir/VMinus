package net.lixir.vminus.mixins.creative;

import net.lixir.vminus.vision.*;
import net.lixir.vminus.vision.util.VisionCreativeOrder;
import net.lixir.vminus.vision.util.VisionItemReplacement;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = CreativeModeTab.class, priority = 12000)
public class CreativeModeTabMixin implements VisionDuck {
    @Unique
    private final CreativeModeTab vMinus$self = (CreativeModeTab) (Object) this;
    @Unique
    private HashMap<Item, Item> WAITING_LIST = new HashMap<>();
    @Unique
    private HashMap<Item, Boolean> WAITING_LIST_ORDER = new HashMap<>();

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "getIconItem", at = @At(value = "RETURN"), cancellable = true)
    private void vMinus$getIconItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack;
        ItemStack iconItemStack = VisionUtil.getOverrideValue(((VisionDuck) vMinus$self), VisionPropertyTypes.Tabs.ICON, null);
        if (iconItemStack != null) {
            itemStack = iconItemStack;
        } else {
            itemStack = cir.getReturnValue();
        }
        // Makes a tab item specifically detectable with its nbt data.
        itemStack.getOrCreateTag().putBoolean("tab_item", true);
        cir.setReturnValue(itemStack);
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "buildContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;rebuildSearchTree()V"))
    private void vMinus$buildContents(CreativeModeTab.ItemDisplayParameters displayContext, CallbackInfo ci) {
        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vMinus$self;

        Vision vision = Vision.getVision((VisionDuck) vMinus$self);
        List<VisionCreativeOrder> visionCreativeOrders = vision.getValues(VisionPropertyTypes.Tabs.ORDER);
        List<VisionItemReplacement> removals = vision.getValues(VisionPropertyTypes.Tabs.REMOVE);

        List<Item> itemsToRemove = new ArrayList<>(removals.stream()
                .filter((VisionItemReplacement t) -> t.itemStack() != null)
                .map((VisionItemReplacement t) -> t.itemStack().getItem())
                .toList());

        List<TagKey<Item>> itemTagsToRemove = new ArrayList<>(removals.stream()
                .map(VisionItemReplacement::tag)
                .filter(Objects::nonNull)
                .toList());


        vminus$processHiddenItems(accessor.getDisplayItems(), itemsToRemove, itemTagsToRemove);
        vminus$processHiddenItems(accessor.getSearchItems(), itemsToRemove, itemTagsToRemove);


        List<VisionCreativeOrder> orders = new ArrayList<>(visionCreativeOrders);
        List<ItemStack> itemList = new ArrayList<>(accessor.getDisplayItems());

        orders.sort(Comparator.comparingInt(order -> {
            ItemStack targetStack = order.getTargetItemStack();
            return targetStack != null ? vminus$findItemIndex(itemList, targetStack.getItem()) : Integer.MAX_VALUE;
        }));


        for (VisionCreativeOrder order : orders) {
            ItemStack itemStack = order.getItemStack();
            TagKey<Item> tagKey = order.getTagKey();
            ItemStack targetItemStack = order.getTargetItemStack();

            if (itemStack != null) {
                Item item = itemStack.getItem();

                if (targetItemStack != null) {
                    Item targetItem = targetItemStack.getItem();
                    vminus$addItemsToTab(targetItem, item, order.isBefore());
                } else {
                    if (!accessor.getDisplayItems().contains(itemStack))
                        accessor.getDisplayItems().add(itemStack);
                    accessor.getSearchItems().add(itemStack);
                }
            } else if (tagKey != null) {
                if (targetItemStack != null) {
                    Item targetItem = targetItemStack.getItem();
                    BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).forEach(taggedItem -> {
                        ItemStack stack = new ItemStack(taggedItem.value());
                        vminus$addItemsToTab(targetItem, stack.getItem(), order.isBefore());
                    });
                } else {
                    BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).forEach(taggedItem -> {
                        ItemStack stack = new ItemStack(taggedItem.value());
                        if (!accessor.getDisplayItems().contains(stack)) {
                            accessor.getDisplayItems().add(stack);
                        }
                        accessor.getSearchItems().add(stack);
                    });
                }
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
        vMinus$self.rebuildSearchTree();
    }

    @Unique
    private void vminus$processHiddenItems(@NotNull Collection<ItemStack> itemStacks, List<Item> itemsToRemove, List<TagKey<Item>> itemTagsToRemove) {
        var tagCollection = ForgeRegistries.ITEMS.tags();

        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null)
                continue;

            Item item = itemStack.getItem();
            VisionDuck visionDuck = (VisionDuck) item;
            Boolean ban = VisionUtil.getOverrideValue(visionDuck, VisionPropertyTypes.Items.BAN, new VisionContext(itemStack));
            VisionItemReplacement visionItemReplacement = VisionUtil.getOverrideValue(visionDuck, VisionPropertyTypes.Items.REPLACE, new VisionContext(itemStack));
            boolean isTaggedForRemoval = itemTagsToRemove.stream().anyMatch(tagKey ->
                    tagCollection != null && tagCollection.getTag(tagKey).contains(item));

            if ((ban != null && ban) ||
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
        Boolean ban = VisionUtil.getOverrideValue(((VisionDuck) item), VisionPropertyTypes.Items.BAN, new VisionContext(item));
        if (ban != null && ban)
            return;
        Boolean targetBan = VisionUtil.getOverrideValue(((VisionDuck) targetItem), VisionPropertyTypes.Items.BAN, new VisionContext(targetItem));
        if (targetBan != null && targetBan)
            return;

        CreativeTabModeAccessor accessor = (CreativeTabModeAccessor) vMinus$self;
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
    private int vminus$findItemIndex(@NotNull List<ItemStack> items, Item targetItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem() == targetItem) {
                return i;
            }
        }
        return -1;
    }

    @Unique
    private boolean vminus$containsItem(@NotNull Collection<ItemStack> items, Item item) {
        for (ItemStack stack : items) {
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.TAB;
    }


    @Override
    public void vMinus$setVisionId(ResourceLocation id) {
        vMinus$visionId = id;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }
}