package net.lixir.vminus.mixins.command;

import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemArgument.class)
public interface ItemArgumentAccessor {
    @Accessor("items")
    HolderLookup<Item> getItems();
}