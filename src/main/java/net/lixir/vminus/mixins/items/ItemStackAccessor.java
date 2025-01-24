package net.lixir.vminus.mixins.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Invoker("expandBlockState")
    Collection<Component> getExpandBlockState(String p_41762_);

    @Invoker("getHideFlags")
    int invokeGetHideFlags();

    @Accessor("count")
    void invokeSetCount(int value);
}