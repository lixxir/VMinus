package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.core.VisionProperties;
import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Unique
    private final ItemEntity vminus$itemEntity = (ItemEntity) (Object) this;

    @Inject(at = @At("RETURN"), method = "fireImmune()Z", cancellable = true)
    private void fireImmune(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().fireResistant.getValue(new VisionConditionArguments.Builder().passItemStack(vminus$itemEntity.getItem()).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Unique
    public ItemVision vminus$getVision() {
        if (vminus$itemEntity.getItem().getItem() instanceof IItemVisionable iVisionable) {
            return iVisionable.vminus$getVision();
        }
        return null;
    }
}
