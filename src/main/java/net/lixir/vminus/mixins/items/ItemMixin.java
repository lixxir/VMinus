package net.lixir.vminus.mixins.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.registry.VMinusRarities;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Unique
    private final Item vminus$item = (Item) (Object) this;

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        if (vminus$item == null || vminus$item.equals(Items.AIR))
            return;
        JsonObject visionData = Vision.getData(vminus$item);
        FoodProperties originalProperties = cir.getReturnValue();
        FoodProperties.Builder modifiedFoodProperties = null;
        if (originalProperties != null) {
            modifiedFoodProperties = new FoodProperties.Builder();
            modifiedFoodProperties.nutrition(originalProperties.getNutrition());
            modifiedFoodProperties.saturationMod(originalProperties.getSaturationModifier());

            List<Pair<MobEffectInstance, Float>> effects = originalProperties.getEffects();

            for (Pair<MobEffectInstance, Float> effectInstance : effects) {
                JsonObject effectData = Vision.getData(effectInstance.getFirst().getEffect());
                if (effectData == null || !effectData.has("banned")) {
                    modifiedFoodProperties.effect(effectInstance.getFirst(), effectInstance.getSecond());
                }
            }

            if (originalProperties.canAlwaysEat())
                modifiedFoodProperties.alwaysEat();
            if (originalProperties.isMeat())
                modifiedFoodProperties.meat();
            if (originalProperties.isFastFood())
                modifiedFoodProperties.fast();
        }
        cir.setReturnValue(VisionProperties.getFoodProperties(visionData, vminus$item, modifiedFoodProperties != null ? modifiedFoodProperties.build() : originalProperties));
    }


    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.STACK_SIZE, vminus$item) != null)
            cir.setReturnValue(Math.max(1, VisionProperties.getNumber(VisionProperties.Names.STACK_SIZE, vminus$item, cir.getReturnValue()).intValue()));
    }


    @Inject(method = "isFireResistant", at = @At("RETURN"), cancellable = true)
    public final void isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.FIRE_RESISTANT, vminus$item) != null)
            cir.setReturnValue(VisionProperties.getBoolean(VisionProperties.Names.FIRE_RESISTANT, vminus$item, cir.getReturnValue()));
    }

    @Inject(method = "isValidRepairItem", at = @At("RETURN"), cancellable = true)
    public void isValidRepairItem(ItemStack p_41402_, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.STACK_SIZE, vminus$item) != null)
            cir.setReturnValue(Math.max(0, VisionProperties.getNumber(VisionProperties.Names.STACK_SIZE, vminus$item, cir.getReturnValue()).intValue()));
    }

    @Inject(method = "isEdible", at = @At("RETURN"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        JsonObject visionData = Vision.getData(vminus$item);
        if (VisionProperties.findSearchObject(VisionProperties.Names.FOOD_PROPERTIES, visionData) != null) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("RETURN"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        if (VisionProperties.findSearchObject(VisionProperties.Names.ENCHANTABILITY, vminus$item) != null)
            cir.setReturnValue(Math.max(0, VisionProperties.getNumber(VisionProperties.Names.ENCHANTABILITY, vminus$item, cir.getReturnValue()).intValue()));
    }

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void getRarity(ItemStack itemstack, CallbackInfoReturnable<Rarity> cir) {
        JsonObject visionData = Vision.getData(itemstack);
        String rarityString = VisionProperties.getString(visionData, "rarity", itemstack);
        if (rarityString != null && !rarityString.isEmpty()) {
            rarityString = rarityString.toUpperCase();
            cir.setReturnValue(
                    switch (rarityString) {
                        case "LEGENDARY":
                            yield VMinusRarities.LEGENDARY;
                        case "UNOBTAINABLE":
                            yield VMinusRarities.UNOBTAINABLE;
                        case "INVERTED":
                            yield VMinusRarities.INVERTED;
                        case "DELICACY":
                            yield VMinusRarities.DELICACY;
                        default:
                            yield Rarity.valueOf(rarityString);
                    }
            );
        }
    }
}
