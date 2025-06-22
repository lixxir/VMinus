package net.lixir.vminus.mixins.items;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionPropertyTypes;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.util.VisionUtil;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IForgeItemStack, VisionDuck {
    @Unique
    private final ItemStack vMinus$self = (ItemStack) (Object) this;

    @Shadow
    public abstract Item getItem();

    @Override
    public int vMinus$getVisionIndex() {
        return ((VisionDuck) getItem()).vMinus$getVisionIndex();
    }

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public final void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        Integer maxDamage = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
        /* Patches out an issue with damage bars not scaling properly when a new durability is applied.
         Only do it with vision applied durability to prevent altering vanilla behavior.
         */
        if (vMinus$self.isDamageableItem() && maxDamage != null && maxDamage > 0) {
            float durabilityRatio = 1.0F - ((float) vMinus$self.getDamageValue() / vMinus$self.getMaxDamage());
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
    }

    @Inject(method = "isEnchantable", at = @At("RETURN"), cancellable = true)
    public final void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.ENCHANTABLE, new VisionContext(vMinus$self));
    }

    @Inject(method = "getDrinkingSound", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        vMinus$trySetEatSound(cir);
    }

    @Inject(method = "getEatingSound", at = @At("RETURN"), cancellable = true)
    public final void vMinus$getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        vMinus$trySetEatSound(cir);
    }

    @Unique
    public final void vMinus$trySetEatSound(CallbackInfoReturnable<SoundEvent> cir) {
        VisionFoodProperties visionFoodProperties = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties == null)
            return;
        SoundEvent eatSound = visionFoodProperties.getEatSound();
        if (eatSound != null)
            cir.setReturnValue(eatSound);
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    public final void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        VisionUtil.tryOverride(cir, this, VisionPropertyTypes.Items.USE_TICKS, new VisionContext(vMinus$self));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        EquipmentSlot equipmentSlot = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.EQUIP_SLOT, new VisionContext(vMinus$self));
        if (equipmentSlot != null)
            return equipmentSlot;
        return IForgeItemStack.super.getEquipmentSlot();
    }

    @Override
    public boolean canEquip(EquipmentSlot armorType, Entity entity) {
        Boolean canEquip = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.CAN_EQUIP, new VisionContext(vMinus$self));
        if (canEquip != null)
            return canEquip;
        EquipmentSlot equipmentSlot = VisionUtil.getOverrideValue(this, VisionPropertyTypes.Items.EQUIP_SLOT, new VisionContext(vMinus$self));
        if (equipmentSlot != null)
            return true;
        return IForgeItemStack.super.canEquip(armorType, entity);
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
