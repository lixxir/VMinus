package net.lixir.vminus.mixins;

import net.lixir.vminus.vision.VisionDuck;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffect.class)
public class MobEffectMixin implements VisionDuck {
    @Unique
    private final MobEffect vminus$mobEffect = (MobEffect) (Object) this;

    @Unique
    private int vMinus$visionIndex = 0;

    /*
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, vminus$getVision().color, new VisionContext(vminus$mobEffect));
    }

    @Inject(method = "getCategory", at = @At("RETURN"), cancellable = true)
    public void getCategory(CallbackInfoReturnable<MobEffectCategory> cir) {
        VisionUtil.tryOverride(cir, vminus$getVision().category,  new VisionContext(vminus$mobEffect));
    }

     */

    @Override
    public void vMinus$setVisionIndex(int index) {
        vMinus$visionIndex = index;
    }

    @Override
    public int vMinus$getVisionIndex() {
        return vMinus$visionIndex;
    }
}
