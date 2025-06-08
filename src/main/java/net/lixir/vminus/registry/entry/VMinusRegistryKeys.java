package net.lixir.vminus.registry.entry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.item.trait.ItemTrait;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class VMinusRegistryKeys {
    public static final ResourceKey<Registry<ItemTrait>> ITEM_TRAIT =
        ResourceKey.createRegistryKey(new ResourceLocation(VMinus.ID, "item_trait"));
}
