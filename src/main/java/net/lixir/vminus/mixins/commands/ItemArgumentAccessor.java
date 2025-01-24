package net.lixir.vminus.mixins.commands;

import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Collection;
import java.util.Set;

@Mixin(ItemArgument.class)
public interface ItemArgumentAccessor {
    @Accessor("items")
    HolderLookup<Item> getitems();
}