package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements VisionDuck, ItemEntryAccessor, MaxDurationGetter, IForgeItem {
    @Unique
    private final Item vMinus$self = (Item) (Object) this;

    @Unique
    private ItemEntry vMinus$itemEntry = null;

    @Unique
    private int vMinus$visionIndex = 0;

    @Inject(method = "isFoil", at = @At("RETURN"), cancellable = true)
    public final void vMinus$isFoil(CallbackInfoReturnable<Boolean> cir) { // Foil is a stupid name so were ganna go with glint.
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.GLINT, new VisionContext(vMinus$self));
    }

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.MAX_STACK_SIZE, new VisionContext(vMinus$self));
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
    }

    @Inject(method = "canBeDepleted", at = @At("RETURN"), cancellable = true)
    public void vMinus$canBeDepleted(CallbackInfoReturnable<Boolean> cir) {
        Integer maxDamage = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
        if (maxDamage != null && maxDamage > 0)
            cir.setReturnValue(true);
    }

    @Inject(method = "getUseAnimation", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getUseAnimation(ItemStack itemStack, CallbackInfoReturnable<UseAnim> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.USE_ANIMATION, new VisionContext(itemStack));
    }


    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        VisionFoodProperties visionFoodProperties = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties != null) {
            cir.setReturnValue(visionFoodProperties.merge(cir.getReturnValue()));
        }
    }

    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    public final void isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.FIRE_RESISTANT, new VisionContext(vMinus$self));
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.USE_TICKS, new VisionContext(itemStack));
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {  // Allow it to be edible if food properties exist. Can not be a value on its own as it will crash without FoodProperties.
        VisionFoodProperties visionFoodProperties = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.ENCHANTABILITY, new VisionContext(vMinus$self));
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void getRarity(ItemStack itemStack, CallbackInfoReturnable<Rarity> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.RARITY, new VisionContext(itemStack));
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        Integer value = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.FUEL_TICKS, new VisionContext(stack));
        if (value != null)
            return value;
        return IForgeItem.super.getBurnTime(stack, recipeType);
    }


    @Override
    public void vminus$setEntry(ItemEntry itemEntry) {
        this.vMinus$itemEntry = itemEntry;
    }

    @Override
    public @Nullable ItemEntry vminus$getEntry() {
        return vMinus$itemEntry;
    }

    @Override
    public int vminus$getMaxDuration() {
        Integer value = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.MAX_USE_TICKS, new VisionContext(vMinus$self));
        if (value != null)
            return value;
        return 0;
    }

    @Override
    public void vMinus$setVisionIndex(int index) {
        vMinus$visionIndex = index;
    }

    @Override
    public int vMinus$getVisionIndex() {
        return vMinus$visionIndex;
    }
}
