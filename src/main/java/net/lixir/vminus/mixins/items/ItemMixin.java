package net.lixir.vminus.mixins.items;

import net.lixir.vminus.item.IMaxDurationGetter;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.accessor.ItemEntryAccessor;
import net.lixir.vminus.util.FoodPropertiesUtil;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.lixir.vminus.vision.util.ItemStackWrapper;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements VisionDuck, ItemEntryAccessor, IMaxDurationGetter, IForgeItem {
    @Unique
    private final Item vMinus$self = (Item) (Object) this;

    @Unique
    private ItemEntry vMinus$itemEntry = null;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    public void vMinus$finishUsingItem(@NotNull ItemStack itemStack, Level p_40685_, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        ItemStackWrapper useRemainder = VisionUtils.getOverrideValue(this, VisionProperties.Items.USE_REMAINDER, new VisionContext(vMinus$self));
        if (useRemainder == null)
            return;
        ItemStack useRemainderStack = useRemainder.itemStack();
        if (useRemainderStack.isEmpty())
            return;

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (!player.getInventory().add(useRemainderStack)) {
                player.drop(useRemainderStack, false);
            }
        }
        cir.setReturnValue(itemStack);
    }


    @Inject(method = "isFoil", at = @At("RETURN"), cancellable = true)
    private void vMinus$isFoil(CallbackInfoReturnable<Boolean> cir) { // Foil is a stupid name so were ganna go with glint.
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.GLINT, new VisionContext(vMinus$self));
    }

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void vMinus$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.MAX_STACK_SIZE, new VisionContext(vMinus$self));
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void vMinus$getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
    }

    @Inject(method = "canBeDepleted", at = @At("RETURN"), cancellable = true)
    private void vMinus$canBeDepleted(CallbackInfoReturnable<Boolean> cir) {
        Integer maxDamage = VisionUtils.getOverrideValue(this, VisionProperties.Items.MAX_DAMAGE, new VisionContext(vMinus$self));
        if (maxDamage != null && maxDamage > 0)
            cir.setReturnValue(true);
    }

    @Inject(method = "getUseAnimation", at = @At("RETURN"), cancellable = true)
    private void vMinus$getUseAnimation(ItemStack itemStack, CallbackInfoReturnable<UseAnim> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.USE_ANIMATION, new VisionContext(itemStack));
    }

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void vMinus$getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        VisionFoodProperties visionFoodProperties = VisionUtils.getOverrideValue(this, VisionProperties.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties != null) {
            cir.setReturnValue(FoodPropertiesUtil.merge(cir.getReturnValue(), visionFoodProperties));
        }
    }

    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    private void vMinus$isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.FIRE_RESISTANT, new VisionContext(vMinus$self));
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void vMinus$getUseDuration(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.USE_TICKS, new VisionContext(itemStack));
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void vMinus$isEdible(CallbackInfoReturnable<Boolean> cir) {
        // Allow it to be edible if food properties exist.
        // Can not be a value on its own as it will crash without a set FoodProperties.
        VisionFoodProperties visionFoodProperties = VisionUtils.getOverrideValue(this, VisionProperties.Items.FOOD, new VisionContext(vMinus$self));
        if (visionFoodProperties != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void vMinus$getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.ENCHANTABILITY, new VisionContext(vMinus$self));
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void vMinus$getRarity(ItemStack itemStack, CallbackInfoReturnable<Rarity> cir) {
        VisionUtils.tryOverride(cir, this, VisionProperties.Items.RARITY, new VisionContext(itemStack));
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        Integer value = VisionUtils.getOverrideValue(this, VisionProperties.Items.FUEL_TICKS, new VisionContext(stack));
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
        Integer value = VisionUtils.getOverrideValue(this, VisionProperties.Items.MAX_USE_TICKS, new VisionContext(vMinus$self));
        if (value != null)
            return value;
        return 0;
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack self) {
        EquipmentSlot equipmentSlot = VisionUtils.getOverrideValue(this, VisionProperties.Items.EQUIP_SLOT, new VisionContext(self));
        if (equipmentSlot != null)
            return equipmentSlot;
        return IForgeItem.super.getEquipmentSlot(self);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        Boolean canEquip = VisionUtils.getOverrideValue(this, VisionProperties.Items.CAN_EQUIP, new VisionContext(stack));
        if (canEquip != null)
            return canEquip;
        EquipmentSlot equipmentSlot = VisionUtils.getOverrideValue(this, VisionProperties.Items.EQUIP_SLOT, new VisionContext(stack));
        if (equipmentSlot != null)
            return true;
        return IForgeItem.super.canEquip(stack, armorType, entity);
    }



    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.ITEM;
    }

    @Override
    public void vMinus$setVisionId(ResourceLocation id) {
        vMinus$visionId = id;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }
}
