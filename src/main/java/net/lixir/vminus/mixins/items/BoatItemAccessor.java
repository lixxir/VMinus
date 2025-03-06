package net.lixir.vminus.mixins.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatItem.class)
public interface BoatItemAccessor {
    @Accessor("hasChest")
    boolean invokeHasChest();
}