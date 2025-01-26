package net.lixir.vminus.mixins.client.entityrenderers;

import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.entity.EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void shouldShowName(T p_114504_, CallbackInfoReturnable<Boolean> cir) {
        if (p_114504_ instanceof LivingEntity _livingentity) {
            ItemStack helmet = _livingentity.getItemBySlot(EquipmentSlot.HEAD);
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.NAMETAG_HIDDEN, helmet, cir.getReturnValue()));
        }
    }
}
