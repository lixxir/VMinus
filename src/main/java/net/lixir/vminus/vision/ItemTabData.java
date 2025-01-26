package net.lixir.vminus.vision;

import net.minecraft.world.item.Item;

public record ItemTabData(Item item, String tabId, Item matchItem, boolean before) { }