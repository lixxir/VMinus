package net.lixir.vminus.registry.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockSetCreativeOrder {
    private final CreativeModeTab creativeTab;
    private final Item targetItem;
    private final Boolean before;
    private final List<RegistryObject<Item>> pendingItems = new ArrayList<>();

    public BlockSetCreativeOrder(CreativeModeTab creativeTab, Item targetItem, Boolean before) {
        this.creativeTab = creativeTab;
        this.targetItem = targetItem;
        this.before = before;
    }

    public CreativeModeTab getCreativeTab() {
        return creativeTab;
    }

    public Item getTarget() {
        return targetItem;
    }

    public Boolean isBefore() {
        return before;
    }

    public void addItemRegistry(RegistryObject<Item> item) {
        pendingItems.add(item);
    }

    public List<Item> getResolvedItems() {
        List<Item> resolvedItems = new ArrayList<>();
        for (RegistryObject<Item> item : pendingItems) {
            if (item.isPresent()) {
                resolvedItems.add(item.get());
            }
        }
        Collections.reverse(resolvedItems);
        return resolvedItems;
    }

}
