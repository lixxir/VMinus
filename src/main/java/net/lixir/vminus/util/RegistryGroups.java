package net.lixir.vminus.util;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class RegistryGroups {
    public static List<Item> getEdibleItems() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(Item::isEdible)
                .collect(Collectors.toList());
    }

}
