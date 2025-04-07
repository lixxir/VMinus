package net.lixir.vminus.mixins.items;

import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionFoodProperties;
import net.lixir.vminus.visions.accessors.IItemVisionAccessor;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin implements IItemVisionAccessor {

    @Unique
    private final Item vminus$item = (Item) (Object) this;

    @Unique
    private ItemVision vminus$itemVision = null;

    /*
    @Inject(method = "getUseAnimation", at = @At("RETURN"), cancellable = true)
    public void getUseAnimation(ItemStack p_41452_, CallbackInfoReturnable<UseAnim> cir) {
        JsonObject visionData = Visions.getData(p_41452_);
        String useAnimationString = VisionProperties.getString(visionData, VisionProperties.Names.USE_ANIMATION, vminus$item);
        if (useAnimationString != null) {
            cir.setReturnValue(UseAnim.valueOf(useAnimationString.toUpperCase()));
        }
    }

     */


    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value.mergeFoodProperties(cir.getReturnValue()));
    }


    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().max_stack_size.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value);
    }


    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    public final void isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().fire_resistant.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value);
    }

    /*
    @Inject(method = "isValidRepairItem", at = @At("RETURN"), cancellable = true)
    public void isValidRepairItem(ItemStack p_41402_, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

    }

     */

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().use_duration.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(true);
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().enchantability.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void getRarity(ItemStack itemStack, CallbackInfoReturnable<Rarity> cir) {
        Rarity value = vminus$getVision().rarity.value(new VisionConditionArguments(vminus$item));
        if (value != null) cir.setReturnValue(value);
    }

    @Override
    public void vminus$setVision(ItemVision itemVision) {
        if (this.vminus$itemVision == null)
            this.vminus$itemVision = itemVision;
        else
            this.vminus$itemVision.merge(itemVision);
    }

    @Override
    public ItemVision vminus$getVision() {
        if (vminus$itemVision == null)
            return ItemVision.EMPTY;
        return this.vminus$itemVision;
    }
}
