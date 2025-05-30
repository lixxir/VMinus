package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.MaxDurationGetter;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionFoodProperties;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.util.VisionUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin implements ItemVisionAccessor, ItemEntryAccessor, MaxDurationGetter {

    @Unique
    private final Item vminus$item = (Item) (Object) this;

    @Unique
    private ItemVision vminus$itemVision = null;

    @Unique
    private ItemEntry vminus$itemEntry = null;

    @Inject(method = "getUseAnimation", at = @At("RETURN"), cancellable = true)
    public void getUseAnimation(ItemStack p_41452_, CallbackInfoReturnable<UseAnim> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().use_animation, vminus$item);
    }

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value.mergeFoodProperties(cir.getReturnValue()));
    }


    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().max_stack_size, vminus$item);
    }


    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    public final void isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().fire_resistant, vminus$item);
    }

    /*
    @Inject(method = "isValidRepairItem", at = @At("RETURN"), cancellable = true)
    public void isValidRepairItem(ItemStack p_41402_, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

    }

     */

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().use_duration, vminus$item);
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$item));
        if (value != null)
            cir.setReturnValue(true);
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().enchantability, vminus$item);
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void getRarity(ItemStack itemStack, CallbackInfoReturnable<Rarity> cir) {
        VisionUtil.visionOverride(cir, vminus$getVision().rarity, vminus$item);
    }

    @Override
    public void vminus$mergeVision(ItemVision itemVision) {
        if (this.vminus$itemVision == null)
            this.vminus$itemVision = itemVision;
        else
            this.vminus$itemVision.merge(itemVision);
    }

    @Override
    public @NotNull ItemVision vminus$getVision() {
        if (vminus$itemVision == null)
            return ItemVision.EMPTY;
        return this.vminus$itemVision;
    }



    @Override
    public void vminus$clearVision() {
        if (vminus$itemVision != null)
            this.vminus$itemVision = new ItemVision();
    }

    @Override
    public void vminus$freezeVision() {
        if (vminus$itemVision != null)
            this.vminus$itemVision.freeze();
    }

    @Override
    public void vminus$setEntry(ItemEntry itemEntry) {
        this.vminus$itemEntry = itemEntry;
    }

    @Override
    public @Nullable ItemEntry vminus$getEntry() {
        return vminus$itemEntry;
    }

    @Override
    public int vminus$getMaxDuration() {
        Integer value = vminus$getVision().max_duration.value(new VisionConditionArguments(vminus$item));
        if (value != null)
            return value;
        return 0;
    }
}
