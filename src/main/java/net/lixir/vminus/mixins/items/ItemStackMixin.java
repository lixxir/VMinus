package net.lixir.vminus.mixins.items;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack, VisionDuck {
    @Unique
    private final ItemStack vMinus$self = (ItemStack) (Object) this;

    @Shadow
    public abstract Item getItem();

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ITEM;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) getItem()).vMinus$getVisionId();
    }

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    private void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        Integer maxDamage = VisionUtils.getOverrideValue(this, VisionProperties.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
        /* Patches out an issue with damage bars not scaling properly when a new durability is applied.
         Only do it with vision applied durability to prevent altering vanilla behavior.
         */
        if (vMinus$self.isDamageableItem() && maxDamage != null && maxDamage > 0) {
            float durabilityRatio = 1.0F - ((float) vMinus$self.getDamageValue() / vMinus$self.getMaxDamage());
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
    }

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    private void vMinus$isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        Integer maxDamage = VisionUtils.getOverrideValue(this, VisionProperties.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
        if (maxDamage != null && vMinus$self.isDamaged()) {
            cir.setReturnValue(true);
        }
    }


    @Inject(method = "isEnchantable", at = @At("RETURN"), cancellable = true)
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.ENCHANTABLE, new VisionContext(vMinus$self));
    }

    @Inject(method = "getDrinkingSound", at = @At("RETURN"), cancellable = true)
    private void vMinus$getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        vMinus$trySetEatSound(cir);
    }

    @Inject(method = "getEatingSound", at = @At("RETURN"), cancellable = true)
    private void vMinus$getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        vMinus$trySetEatSound(cir);
    }

    @Unique
    private void vMinus$trySetEatSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties visionFoodProperties = VisionUtils.getOverrideValue(this, VisionProperties.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties == null)
            return;
        SoundEvent eatSound = visionFoodProperties.eatSound();
        if (eatSound != null)
            cir.setReturnValue(eatSound);
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.USE_TICKS, new VisionContext(vMinus$self));
    }




    /*
    @Override
    public boolean makesPiglinsNeutral(LivingEntity wearer) {
        if (ItemTraits.hasTrait(vMinus$self, ItemTraits.PIGLIN_CHARM.get()))
            return (ItemTraits.getTrait(vMinus$self, ItemTraits.PIGLIN_CHARM.get()));
        return vMinus$self.getItem().makesPiglinsNeutral(vMinus$self, wearer);
    }
     */

}
