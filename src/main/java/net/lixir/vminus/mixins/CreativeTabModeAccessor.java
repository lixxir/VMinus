package net.lixir.vminus.mixins;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public interface CreativeTabModeAccessor {
    @Accessor("displayItems")
    Collection<ItemStack> getDisplayItems();

    @Accessor("displayItemsSearchTab")
    Set<ItemStack> getSearchItems();
}