package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    private final Entity vminus$entity = (Entity) (Object) this;

    @Inject(method = "isSilent", at = @At("RETURN"), cancellable = true)
    private void isSilent(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.SILENT, vminus$entity) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.SILENT, vminus$entity, cir.getReturnValue()));
    }

    @Inject(method = "dampensVibrations", at = @At("RETURN"), cancellable = true)
    private void dampensVibrations(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.DAMPENS_VIBRATIONS, vminus$entity) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.DAMPENS_VIBRATIONS, vminus$entity, cir.getReturnValue()));
    }
}
