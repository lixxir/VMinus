package net.lixir.vminus.mixins.items;

import net.minecraft.world.item.BoatItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatItem.class)
public interface BoatItemAccessor {
    @Accessor("hasChest")
    boolean invokeHasChest();
}