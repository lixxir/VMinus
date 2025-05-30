package net.lixir.vminus.mixins;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Slot.class)
public interface SlotAccessor {
    @Accessor("slot")
    int getSlot();
}