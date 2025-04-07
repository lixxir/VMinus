package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Unique
    private final ItemEntity vminus$itemEntity = (ItemEntity) (Object) this;

    @Inject(at = @At("RETURN"), method = "fireImmune()Z", cancellable = true)
    private void vminus$fireImmune(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = ItemVision.of(vminus$itemEntity).fire_resistant.value(new VisionConditionArguments(vminus$itemEntity));
        if (value != null && value) cir.setReturnValue(true);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void vminus$tick(CallbackInfo ci) {
        ItemVision itemVision = ItemVision.of(vminus$itemEntity);
        Boolean ban = itemVision.ban.value(new VisionConditionArguments(vminus$itemEntity));
        VisionItemReplacement visionItemReplacement = itemVision.replace.value(new VisionConditionArguments(vminus$itemEntity));
        if ((ban != null && ban) || (visionItemReplacement != null && visionItemReplacement.itemStack() != null))
            vminus$itemEntity.kill();
    }
}
