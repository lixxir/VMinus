package net.lixir.vminus.mixins;

import net.lixir.vminus.visions.EffectVision;
import net.lixir.vminus.visions.accessors.EffectVisionAccessor;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public class MobEffectMixin implements EffectVisionAccessor {
    @Unique
    private final MobEffect vminus$mobEffect = (MobEffect) (Object) this;

    @Unique
    private EffectVision vminus$effectVision = null;

    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, vminus$getVision().color, new VisionConditionArguments(vminus$mobEffect));
    }

    @Inject(method = "getCategory", at = @At("RETURN"), cancellable = true)
    public void getCategory(CallbackInfoReturnable<MobEffectCategory> cir) {
        VisionUtil.tryOverride(cir, vminus$getVision().category,  new VisionConditionArguments(vminus$mobEffect));
    }

    @Override
    public @NotNull EffectVision vminus$getVision() {
        if (vminus$effectVision == null)
            return EffectVision.EMPTY;
        return this.vminus$effectVision;
    }

    @Override
    public void vminus$mergeVision(EffectVision vision) {
        if (this.vminus$effectVision == null)
            this.vminus$effectVision = vision;
        else
            this.vminus$effectVision.merge(vminus$effectVision);
    }

    @Override
    public void vminus$freezeVision() {
        if (vminus$effectVision != null)
            this.vminus$effectVision.freeze();
    }

    @Override
    public void vminus$clearVision() {
        if (vminus$effectVision != null)
            this.vminus$effectVision = new EffectVision();
    }
}
