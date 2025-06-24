package net.lixir.vminus.mixins.enchantments;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.EnchantmentVisionHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements VisionDuck {
    @Unique
    private final Enchantment vMinus$self = (Enchantment) (Object) this;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "getMinLevel", at = @At("RETURN"), cancellable = true)
    private void getMinLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getMinLevel(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : 1));
    }

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getMaxLevel(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : 1));
    }

    @Inject(method = "isTreasureOnly", at = @At("RETURN"), cancellable = true)
    private void isTreasureOnly(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isTreasure(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isCurse", at = @At("RETURN"), cancellable = true)
    private void isCurse(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isCurse(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
    private void canEnchant(ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.canEnchant(vMinus$self, itemstack, cir.getReturnValue()));
    }

    @Inject(method = "isCompatibleWith", at = @At("RETURN"), cancellable = true)
    private void isCompatibleWith(Enchantment otherEnchantment, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isCompatible(vMinus$self, otherEnchantment, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isTradeable", at = @At("RETURN"), cancellable = true)
    private void isTradeable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isTradeable(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "isDiscoverable", at = @At("RETURN"), cancellable = true)
    private void isDiscoverable(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.isDiscoverable(vMinus$self, cir.getReturnValue() != null ? cir.getReturnValue() : false));
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void getRarity(CallbackInfoReturnable<Enchantment.Rarity> cir) {
        cir.setReturnValue(EnchantmentVisionHelper.getRarity(vMinus$self, cir.getReturnValue()));
    }

    @Override
    public void vMinus$setVisionId(@Nullable ResourceLocation id) {
        this.vMinus$visionId = id;
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ENCHANTMENT;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }
}
