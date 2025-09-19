package net.lixir.vminus.mixins.items;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.vision.*;
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

    @ModifyReturnValue(method = "getBarWidth", at = @At("RETURN"))
    private int getBarWidth(int original) {
        Integer maxDamage = Vision.getValue(vMinus$self, VisionProperties.Items.MAX_DAMAGE);
        /* Patches out an issue with damage bars not scaling properly when a new durability is applied.
         Only do it with vision applied durability to prevent altering vanilla behavior.
         */
        if (vMinus$self.isDamageableItem() && maxDamage != null && maxDamage > 0) {
            float durabilityRatio = 1.0F - ((float) vMinus$self.getDamageValue() / vMinus$self.getMaxDamage());
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            return Math.min(barWidth, 13);
        }
        return original;
    }

    @ModifyReturnValue(method = "isBarVisible", at = @At("RETURN"))
    private boolean vMinus$isBarVisible(boolean original) {
        Integer maxDamage = Vision.getValue(vMinus$self, VisionProperties.Items.MAX_DAMAGE);
        if (maxDamage != null && vMinus$self.isDamaged())
            return true;
        return original;
    }

    @ModifyReturnValue(method = "isEnchantable", at = @At("RETURN"))
    private boolean vMinus$isEnchantable(boolean original) {
       return Vision.getValue(vMinus$self, VisionProperties.Items.ENCHANTABLE, original);
    }

    @ModifyReturnValue(method = "getDrinkingSound", at = @At("RETURN"))
    private SoundEvent vMinus$getDrinkingSound(SoundEvent original) {
        return vMinus$trySetEatSound(original);
    }

    @ModifyReturnValue(method = "getEatingSound", at = @At("RETURN"))
    private SoundEvent vMinus$getEatingSound(SoundEvent original) {
        return vMinus$trySetEatSound(original);
    }

    @Unique
    private SoundEvent vMinus$trySetEatSound(SoundEvent original) {
        VisionFoodProperties visionFoodProperties = Vision.getValue(vMinus$self, VisionProperties.Items.FOOD);
        if (visionFoodProperties == null)
            return original;
        SoundEvent eatSound = visionFoodProperties.eatSound();
        if (eatSound != null)
            return eatSound;
        return original;
    }

    @ModifyReturnValue(method = "getUseDuration", at = @At("RETURN"))
    private int getUseDuration(int original) {
        return Vision.getValue(vMinus$self, VisionProperties.Items.USE_TICKS, original);
    }
}
