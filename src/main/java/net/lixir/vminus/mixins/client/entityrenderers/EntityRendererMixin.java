package net.lixir.vminus.mixins.client.entityrenderers;

import net.lixir.vminus.registry.VMinusAttributes;
import net.lixir.vminus.traits.Traits;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
    private void shouldShowName(T entity, CallbackInfoReturnable<Boolean> cir) {
        float translucency = entity.getPersistentData().getFloat(VMinusAttributes.TRANSLUCENCE_KEY)*1.5f;
        if (translucency > 0.5f)
                cir.setReturnValue(false);

        if (entity instanceof LivingEntity livingEntity) {
            ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
            if (Traits.hasTrait(helmet, Traits.VEIL.get()))
                cir.setReturnValue(false);
        }
    }
}
