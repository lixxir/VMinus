package net.lixir.vminus.mixins.items;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.world.item.IMaxDurationGetter;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.api.registry.definition.duck.ItemDefinitionDuck;
import net.lixir.vminus.vision.*;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements VisionDuck, ItemDefinitionDuck, IMaxDurationGetter, IForgeItem {
    @Unique
    private final Item vMinus$self = (Item) (Object) this;

    @Unique
    private ItemDefinition vMinus$definition = null;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @Inject(method = "finishUsingItem", at = @At("RETURN"), cancellable = true)
    private void vMinus$finishUsingItem(@NotNull ItemStack itemStack, Level p_40685_, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
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

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void vMinus$isEdible(CallbackInfoReturnable<Boolean> cir) {
        if (Vision.getValue(vMinus$self, VisionProperties.Items.FOOD) != null)
            cir.setReturnValue(true);
    }

    @ModifyReturnValue(method = "getFoodProperties", at = @At("RETURN"))
    private FoodProperties vMinus$foodProperties(FoodProperties original) {
        VisionFoodProperties visionFoodProperties = Vision.getValue(vMinus$self, VisionProperties.Items.FOOD);
        if (visionFoodProperties != null)
            return visionFoodProperties.merge(original);
        return original;
    }

    @ModifyReturnValue(method = "isFoil", at = @At("RETURN"))
    private boolean vMinus$isFoil(boolean original) { // Foil is a stupid key so were ganna go with glint
        return Vision.getValue(vMinus$self, VisionProperties.Items.GLINT, original);
    }

    @ModifyReturnValue(method = "getUseAnimation", at = @At("RETURN"))
    private UseAnim vMinus$getUseAnimation(UseAnim original) {
        return Vision.getValue(vMinus$self, VisionProperties.Items.USE_ANIMATION, original);
    }

    @ModifyReturnValue(method = "getUseDuration", at = @At("RETURN"))
    private int vMinus$getUseDuration(int original) {
        return Vision.getValue(vMinus$self, VisionProperties.Items.USE_TICKS, original);
    }

    @ModifyReturnValue(method = "getEnchantmentValue", at = @At("RETURN"))
    private int vMinus$getEnchantmentValue(int original) {
        return Vision.getValue(vMinus$self, VisionProperties.Items.ENCHANTABILITY, original);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        Integer value = VisionUtils.getOverrideValue(this, VisionProperties.Items.FUEL_TICKS, new VisionContext(stack));
        if (value != null)
            return value;

        return IForgeItem.super.getBurnTime(stack, recipeType);
    }

    @Override
    public void vMinus$setDefinition(@Nullable ItemDefinition definition) {
        this.vMinus$definition = definition;
    }

    @javax.annotation.Nullable
    @Override
    public @Nullable ItemDefinition vMinus$getDefinition() {
        return vMinus$definition;
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
