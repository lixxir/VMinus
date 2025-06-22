package net.lixir.vminus.mixins.entities;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Unique
    private final ItemEntity vminus$itemEntity = (ItemEntity) (Object) this;

    /*
    @Inject(at = @At("RETURN"), method = "fireImmune()Z", cancellable = true)
    private void vminus$fireImmune(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = ItemVision.of(vminus$itemEntity).fire_resistant.value(new VisionContext(vminus$itemEntity));
        if (value != null && value) cir.setReturnValue(true);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void vminus$tick(CallbackInfo ci) {
        ItemVision itemVision = ItemVision.of(vminus$itemEntity);
        Boolean ban = itemVision.ban.value(new VisionContext(vminus$itemEntity));
        VisionItemReplacement visionItemReplacement = itemVision.replace.value(new VisionContext(vminus$itemEntity));
        if ((ban != null && ban) || (visionItemReplacement != null && visionItemReplacement.itemStack() != null))
            vminus$itemEntity.kill();
    }

     */
}
