package net.lixir.vminus.mixins.world.effect;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public class MobEffectMixin implements VisionDuck {
    @Unique
    private final MobEffect vMinus$self = (MobEffect) (Object) this;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void vMinus$getColor(CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Effects.COLOR, new VisionContext(vMinus$self));
    }

    @Inject(method = "getCategory", at = @At("RETURN"), cancellable = true)
    private void vMinus$getCategory(CallbackInfoReturnable<MobEffectCategory> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Effects.CATEGORY, new VisionContext(vMinus$self));
    }

    @Override
    public void vMinus$setVisionId(ResourceLocation id) {
        vMinus$visionId = id;
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.EFFECT;
    }

    @Override
    public ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }
}
