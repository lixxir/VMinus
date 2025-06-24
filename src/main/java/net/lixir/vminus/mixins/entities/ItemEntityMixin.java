package net.lixir.vminus.mixins.entities;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionItemReplacement;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements VisionDuck {
    @Unique
    private final ItemEntity vMinus$self = (ItemEntity) (Object) this;

    @Shadow
    public abstract ItemStack getItem();

    @Inject(at = @At("RETURN"), method = "fireImmune", cancellable = true)
    private void vMinus$fireImmune(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.FIRE_RESISTANT, new VisionContext(vMinus$self));
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void vMinus$tick(CallbackInfo ci) {
        Boolean ban = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.BAN, new VisionContext(vMinus$self));
        VisionItemReplacement replacement = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.REPLACE, new VisionContext(vMinus$self));
        if ((ban != null && ban) || (replacement != null && replacement.itemStack() != null))
            vMinus$self.kill(); // Kill the item if it is banned and has no replacement.
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getItem().getItem()).vMinus$getVisionId();
    }
}
