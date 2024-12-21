package net.lixir.vminus.mixins.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionPropertyHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Unique
    private final Item item = (Item) (Object) this;
    @Shadow
    private Rarity rarity;

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "stack_size");
        if (!propertyMet.isEmpty()) {
            int newStackSize = VisionValueHandler.isNumberMet(itemData, propertyMet, cir.getReturnValue() != null ? cir.getReturnValue() : 64, itemstack);
            cir.setReturnValue(Math.max(newStackSize, 1));
        }
    }

    @Inject(method = "getFoodProperties", at = @At("RETURN"), cancellable = true)
    private void getFoodProperties(CallbackInfoReturnable<FoodProperties> cir) {
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        FoodProperties originalProperties = cir.getReturnValue();
        FoodProperties.Builder modifiedFoodProperties = null;
        if (originalProperties != null) {
            modifiedFoodProperties = new FoodProperties.Builder();
            modifiedFoodProperties.nutrition(originalProperties.getNutrition());
            modifiedFoodProperties.saturationMod(originalProperties.getSaturationModifier());

            List<Pair<MobEffectInstance, Float>> effects = originalProperties.getEffects();

            for (Pair<MobEffectInstance, Float> effectInstance : effects) {
                JsonObject visionData = VisionHandler.getVisionData(effectInstance.getFirst().getEffect());
                if (visionData != null && visionData.has("banned")) {
                    continue;
                } else {
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
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("fire_resistant")) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, "fire_resistant", itemstack));
        }
    }

    @Inject(method = "isValidRepairItem", at = @At("HEAD"), cancellable = true)
    public void isValidRepairItem(ItemStack p_41402_, ItemStack p_41403_, CallbackInfoReturnable<Boolean> cir) {
        JsonObject itemData = VisionHandler.getVisionData(p_41402_);
        if (itemData != null && itemData.has("repair_item")) {
            JsonArray repairMaterialsJsonArray = itemData.getAsJsonArray("repair_item");
            List<String> repairMaterials = VisionValueHandler.getStringListFromJsonArray(repairMaterialsJsonArray);
            String repairItemId = ForgeRegistries.ITEMS.getKey(p_41403_.getItem()).toString();
            if (repairMaterials.contains(repairItemId)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
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
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("food_properties")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("HEAD"), cancellable = true)
    private void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = new ItemStack(item);
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("enchantability")) {
            cir.setReturnValue(VisionValueHandler.isNumberMet(itemData, "enchantability", cir.getReturnValue(), itemstack));
        }
    }

    /**
     * @author lixir
     * @Reason To allow for customized rarities and more dynamic rarity changing
     */
    @Overwrite
    public Rarity getRarity(ItemStack itemstack) {
        CompoundTag tag = itemstack.getTag();
        int rarityLevel = 0;
        String rarityString = "common";
        Rarity currentRarity = this.rarity;
        if (currentRarity == Rarity.UNCOMMON) {
            rarityString = "uncommon";
        } else if (currentRarity == Rarity.RARE) {
            rarityString = "rare";
        } else if (currentRarity == Rarity.EPIC) {
            rarityString = "epic";
        }
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("rarity")) {
            rarityString = VisionValueHandler.getRarity(itemData, itemstack, rarityString).toLowerCase();
        }
        switch (rarityString) {
            case "uncommon":
                rarityLevel = 1;
                break;
            case "rare":
                rarityLevel = 2;
                break;
            case "epic":
                rarityLevel = 3;
                break;
            case "common":
            default:
                rarityLevel = 0;
                break;
        }
        if (tag != null) {
			/*
				double reinforced = tag.getDouble("reinforced");
				double shimmer = tag.getDouble("shimmer");
				if (reinforced >= 4 || shimmer >= 4) {
					rarityLevel += 2;
				} else if (reinforced > 0 || shimmer > 0) {
					rarityLevel++;
				}
				*/
            if (itemstack.isEnchanted()) {
                if (ModList.get().isLoaded("detour")) {
                    rarityLevel++;
                } else {
                    rarityLevel += 2;
                }
            }
        }
        switch (rarityLevel) {
            case 1:
                return Rarity.UNCOMMON;
            case 2:
                return Rarity.RARE;
            case 3:
                return Rarity.EPIC;
            default:
                return Rarity.COMMON;
        }
    }
}
