package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.lixir.vminus.visions.util.VisionFoodProperties;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack {
    @Unique
    private final ItemStack vminus$itemStack = (ItemStack) (Object) this;

    @Unique
    public ItemVision vminus$getVision() {
        if (vminus$itemStack.getItem() instanceof ItemVisionAccessor iVisionable) {
            return iVisionable.vminus$getVision();
        }
        return null;
    }

    @Override
    public boolean makesPiglinsNeutral(LivingEntity wearer) {
        if (ItemTraits.hasTrait(vminus$itemStack, ItemTraits.PIGLIN_CHARM.get()))
            return (ItemTraits.getTrait(vminus$itemStack, ItemTraits.PIGLIN_CHARM.get()));
        return vminus$itemStack.getItem().makesPiglinsNeutral(vminus$itemStack, wearer);
    }

    @Override
    public int getBurnTime(@Nullable RecipeType<?> recipeType) {
        Integer value = vminus$getVision().fuel_time.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) return value;
        return vminus$itemStack.getItem().getBurnTime(vminus$itemStack, recipeType);
    }


    @Override
    public EquipmentSlot getEquipmentSlot() {
        EquipmentSlot value = vminus$getVision().equip_slot.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) return value;
        return vminus$itemStack.getItem().getEquipmentSlot(vminus$itemStack);
    }

    @Override
    public boolean canEquip(EquipmentSlot armorType, Entity entity) {
        Boolean value = vminus$getVision().can_equip.value(new VisionConditionArguments.Builder().pass(vminus$itemStack).pass(entity).build());
        if (value != null) return value;
        EquipmentSlot equipValue = vminus$getVision().equip_slot.value(new VisionConditionArguments.Builder().pass(vminus$itemStack).pass(entity).build());
        if (equipValue != null) return true;
        return vminus$itemStack.getItem().canEquip(vminus$itemStack, armorType, entity);
    }


    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
                int numberOfSlots = capability.getSlots();
                double amount = 0;
                for (int i = 0; i < numberOfSlots; i++) {
                    ItemStack itemStackInSlot = capability.getStackInSlot(i);
                    amount += (double) itemStackInSlot.getCount() / (double) itemStackInSlot.getMaxStackSize();
                }
                float fullness = (float) amount / numberOfSlots;
                int barWidth = (int) Math.floor(13.0F * fullness);
                cir.setReturnValue(Math.min(barWidth, 13));
            });
        }
        /*
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            float durabilityRatio = (float) vminus$itemStack.getTag().getInt("reinforcement") / (float) vminus$itemStack.getTag().getInt("max_reinforcement");
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
        */
        if (vminus$itemStack.isDamageableItem()) {
            float durabilityRatio = 1.0F - ((float) vminus$itemStack.getDamageValue() / vminus$itemStack.getMaxDamage());
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }


    }

    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        /*
        JsonObject itemData = Visions.getData(vminus$itemStack);
        if (itemData != null && itemData.has("bar")) {
            int startColor = 4384126;
            int endColor = 2186818;
            try {
                String startColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "start_color");
                if (startColorString != null)
                    startColor = Integer.decode(startColorString.trim());
            } catch (NumberFormatException e) {
                VMinus.LOGGER.error("Invalid start_color format: " + itemData.get("start_color").getAsString());
            }
            try {
                String endColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "end_color");
                if (endColorString != null)
                    endColor = Integer.decode(endColorString.trim());
            } catch (NumberFormatException e) {
                VMinus.LOGGER.error("Invalid end_color format: " + itemData.get("end_color").getAsString());
            }
            float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
            int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
            cir.setReturnValue(transitionColor);
            cir.cancel();
        } else {
            if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
                vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                    int numberOfSlots = capability.getSlots();
                    int totalItems = 0;
                    int maxCapacity = 0;
                    for (int i = 0; i < numberOfSlots; i++) {
                        ItemStack itemStackInSlot = capability.getStackInSlot(i);
                        totalItems += itemStackInSlot.getCount();
                        maxCapacity += itemStackInSlot.getMaxStackSize();
                    }
                    float fullness = maxCapacity > 0 ? (float) totalItems / maxCapacity : 0;
                    int containerItemColor = vminus$rgbToColor(0.4F, 0.4F, 1.0F);
                    cir.setReturnValue(containerItemColor);
                });
            }
            if (vminus$itemStack.isDamageableItem()) {
                if (vminus$itemStack.getTag().getBoolean("broken")) {
                    cir.setReturnValue(Mth.hsvToRgb(0.01F, 0.0F, 0.35F));
                } else if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
                    int startColor = 0x55FFFF;
                    int endColor = 0x22a53f;
                    float durabilityRatio = (float) vminus$itemStack.getTag().getInt("reinforcement") / (float) vminus$itemStack.getTag().getInt("max_reinforcement");
                    int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
                    cir.setReturnValue(transitionColor);
                } else {

                }
            }
        }

         */

        /*
        int startColor;
        int endColor;
        float durabilityRatio = 1.0F - ((float) vminus$itemStack.getDamageValue() / vminus$itemStack.getMaxDamage());
        if (vminus$itemStack.getTag().getBoolean("death_durability")) {
            startColor = 0xFF00FF;
            endColor = 0x550055;
        } else {
            startColor = 0x69fc2a;
            endColor = 0xe22626;
        }
        int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
        cir.setReturnValue(transitionColor);

         */
    }

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    public void enchant(Enchantment enchantment, int level, CallbackInfo ci) {
        if (EnchantmentVisionHelper.isBanned(enchantment)) {
            ci.cancel();
            return;
        }
        ItemStack itemstack = (ItemStack) (Object) this;
        CompoundTag tag = itemstack.getOrCreateTag();
        if (!tag.contains("enchantment_limit"))
            return;
        int enchantmentLimit = tag.getInt("enchantment_limit");
        double currentTotalEnchantmentLevel = 0.0;
        if (itemstack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                currentTotalEnchantmentLevel += entry.getValue();
            }
        }
        if (currentTotalEnchantmentLevel + level > enchantmentLimit)
            ci.cancel();
    }

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
                boolean hasItems = false;
                for (int i = 0; i < capability.getSlots(); i++) {
                    if (capability.getStackInSlot(i).getCount() > 0) {
                        hasItems = true;
                        break;
                    }
                }
                cir.setReturnValue(hasItems);
            });
        }
        /*
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            if (vminus$itemStack.getTag().getInt("reinforcement") < vminus$itemStack.getTag().getInt("max_reinforcement")) {
                cir.setReturnValue(true);
            }
        } else if (DurabilityHelper.getDurability(vminus$itemStack) < DurabilityHelper.getDurability(true, vminus$itemStack)) {
            cir.setReturnValue(true);
        }

         */
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().max_damage.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isDamageableItem", at = @At("RETURN"), cancellable = true)
    public void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().damageable.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(value);
        Integer maxDamageValue = vminus$getVision().max_damage.value(new VisionConditionArguments(vminus$itemStack));
        if (maxDamageValue != null) cir.setReturnValue(maxDamageValue > 0);
    }

    @Inject(method = "isEnchantable", at = @At("RETURN"), cancellable = true)
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().enchantable.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(true);
    }

    @Inject(method = "getDrinkingSound", at = @At("RETURN"), cancellable = true)
    private void getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null && value.getEatSound() != null)
            cir.setReturnValue(value.getEatSound());
    }

    @Inject(method = "hasFoil", at = @At("RETURN"), cancellable = true)
    private void hasFoil(CallbackInfoReturnable<Boolean> cir) {
        Boolean value = vminus$getVision().glint.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getEatingSound", at = @At("RETURN"), cancellable = true)
    private void getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties value = vminus$getVision().food_properties.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null && value.getEatSound() != null)
            cir.setReturnValue(value.getEatSound());
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        Integer value = vminus$getVision().use_duration.value(new VisionConditionArguments(vminus$itemStack));
        if (value != null) cir.setReturnValue(value);
    }

}
