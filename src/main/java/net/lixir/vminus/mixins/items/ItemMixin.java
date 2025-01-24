package net.lixir.vminus.mixins.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.registry.VMinusRarities;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.util.VisionPropertyNameHandler;
import net.lixir.vminus.vision.util.VisionValueHandler;
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

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "stack_size");
        if (!propertyMet.isEmpty()) {
            int newStackSize = VisionValueHandler.isNumberMet(itemData, propertyMet, cir.getReturnValue() != null ? cir.getReturnValue() : 64, itemstack);
            cir.setReturnValue(Math.max(newStackSize, 1));
        }
    }

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        if (vminus$item == null || vminus$item.equals(Items.AIR))
            return;
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        FoodProperties originalProperties = cir.getReturnValue();
        FoodProperties.Builder modifiedFoodProperties = null;
        if (originalProperties != null) {
            modifiedFoodProperties = new FoodProperties.Builder();
            modifiedFoodProperties.nutrition(originalProperties.getNutrition());
            modifiedFoodProperties.saturationMod(originalProperties.getSaturationModifier());

            List<Pair<MobEffectInstance, Float>> effects = originalProperties.getEffects();

            for (Pair<MobEffectInstance, Float> effectInstance : effects) {
                JsonObject visionData = Vision.getData(effectInstance.getFirst().getEffect());
                if (visionData == null || !visionData.has("banned")) {
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
        if (itemData != null && itemData.has("food_properties")) {
            cir.setReturnValue(VisionValueHandler.getFoodProperties(itemData, itemstack, modifiedFoodProperties != null ? modifiedFoodProperties.build() : originalProperties));
        } else if (modifiedFoodProperties != null) {
            cir.setReturnValue(modifiedFoodProperties.build());
        }
    }

    @Inject(method = "isFireResistant", at = @At("HEAD"), cancellable = true)
    public final void isFireResistant(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        if (itemData != null && itemData.has("fire_resistant")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, "fire_resistant", itemstack));
        }
    }

    @Inject(method = "isValidRepairItem", at = @At("HEAD"), cancellable = true)
    public void isValidRepairItem(ItemStack p_41402_, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        JsonObject itemData = Vision.getData(itemStack);
        if (itemData != null && itemData.has("repair_item")) {
            JsonArray repairMaterialsJsonArray = itemData.getAsJsonArray("repair_item");
            List<String> repairMaterials = VisionValueHandler.getStringListFromJsonArray(repairMaterialsJsonArray);
            String repairItemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString();
            if (repairMaterials.contains(repairItemId)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        if (itemData != null && itemData.has("use_duration")) {
            int defaultDuration = 32;
            int calculatedDuration = VisionValueHandler.isNumberMet(itemData, "use_duration", defaultDuration, itemstack);
            if (calculatedDuration != defaultDuration) {
                cir.setReturnValue(calculatedDuration);
            }
        }
    }

    @Inject(method = "isEdible", at = @At("HEAD"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        if (itemData != null && itemData.has("food_properties")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("HEAD"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(vminus$item);
        JsonObject itemData = Vision.getData(itemstack);
        if (itemData != null && itemData.has("enchantability")) {
            cir.setReturnValue(VisionValueHandler.isNumberMet(itemData, "enchantability", cir.getReturnValue(), itemstack));
        }
    }

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
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
                        default:
                           yield  Rarity.valueOf(rarityString);
                    }
            );
        }
    }
}
