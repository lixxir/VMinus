package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Unique
    private final ItemEntity vminus$item = (ItemEntity) (Object) this;

    @Inject(at = @At("RETURN"), method = "fireImmune()Z", cancellable = true)
    private void fireImmune(CallbackInfoReturnable<Boolean> cir) {

        if (VisionProperties.findSearchObject(VisionProperties.Names.FIRE_RESISTANT, vminus$item) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.FIRE_RESISTANT, vminus$item, cir.getReturnValue()));
    }
}
