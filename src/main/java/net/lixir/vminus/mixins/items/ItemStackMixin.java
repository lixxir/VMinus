package net.lixir.vminus.mixins.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.helpers.DurabilityHelper;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionPropertyNameHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private final ItemStack vminus$itemStack = (ItemStack) (Object) this;

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract Rarity getRarity();

    @Unique
    private int vminus$key = VisionHandler.EMPTY_KEY;

    @Inject(method = "hurt", at = @At(value = "RETURN"), cancellable = true)
    public void vminus$hurt(int p_41630_, Random p_41631_, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (vminus$key == -1)
                vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
            JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
            String replaceId = VisionValueHandler.getFirstValidString(itemData, "break_replacement", vminus$itemStack);
            if (replaceId != null && !replaceId.isEmpty()) {
                final ItemStack findItem = vminus$itemStack;
                CompoundTag tag = findItem.getOrCreateTag();
                ItemStack replacementStack = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(replaceId))));
                int slotIndex = player.getInventory().findSlotMatchingItem(findItem);
                if (slotIndex == -1) {
                    for (int j = 0; j < player.getInventory().armor.size(); j++) {
                        if (player.getInventory().armor.get(j).equals(findItem)) {
                            slotIndex = 100 + j;
                            break;
                        }
                    }
                }
                if (slotIndex == -1) {
                    if (player.getInventory().offhand.get(0).equals(findItem)) {
                        slotIndex = 150;
                    }
                }
                player.awardStat(Stats.ITEM_BROKEN.get(findItem.getItem()));
                if (slotIndex != -1) {
                    if (VisionValueHandler.isBooleanMet(null, "break_replacement", vminus$itemStack, "carry_nbt")) {
                        replacementStack.setTag(tag);
                    }
                    if (slotIndex < 100) {
                        player.getInventory().setItem(slotIndex, replacementStack);
                    } else if (slotIndex < 150) {
                        player.getInventory().armor.set(slotIndex - 100, replacementStack);
                    } else {
                        player.getInventory().offhand.set(0, replacementStack);
                    }
                } else {
                    vminus$itemStack.shrink(1);
                }
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(capability -> {
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
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            float durabilityRatio = (float) vminus$itemStack.getTag().getInt("reinforcement") / (float) vminus$itemStack.getTag().getInt("max_reinforcement");
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        } else if (vminus$itemStack.isDamageableItem()) {
            float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
    }


    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        if (itemData != null && itemData.has("bar")) {
            int startColor = 4384126;
            int endColor = 2186818;
            try {
                String startColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "start_color");
                if (startColorString != null)
                    startColor = Integer.decode(startColorString.trim());
            } catch (NumberFormatException e) {
                VMinusMod.LOGGER.error("Invalid start_color format: " + itemData.get("start_color").getAsString());
            }
            try {
                String endColorString = VisionValueHandler.getFirstValidString(itemData, "bar", vminus$itemStack, "end_color");
                if (endColorString != null)
                    endColor = Integer.decode(endColorString.trim());
            } catch (NumberFormatException e) {
                VMinusMod.LOGGER.error("Invalid end_color format: " + itemData.get("end_color").getAsString());
            }
            float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
            int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
            cir.setReturnValue(transitionColor);
            cir.cancel();
        } else {
            if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
                vminus$itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(capability -> {
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
                    int startColor;
                    int endColor;
                    float durabilityRatio = (float) DurabilityHelper.getDurability(vminus$itemStack) / (float) DurabilityHelper.getDurability(true, vminus$itemStack);
                    if (vminus$itemStack.getTag().getBoolean("death_durability")) {
                        startColor = 0xFF00FF;
                        endColor = 0x550055;
                    } else {
                        startColor = 0x69fc2a;
                        endColor = 0xe22626;
                    }
                    int transitionColor = vminus$interpolateColor(endColor, startColor, durabilityRatio);
                    cir.setReturnValue(transitionColor);
                }
            }
        }
    }

    @Inject(method = "enchant", at = @At("HEAD"), cancellable = true)
    public void enchant(Enchantment enchantment, int level, CallbackInfo ci) {
        if (EnchantmentVisionHelper.isBanned(enchantment)) {
            ci.cancel();
            return;
        }
        ItemStack itemstack = (ItemStack) (Object) this;
        CompoundTag tag = itemstack.getOrCreateTag();
        int enchLimit = tag.contains("enchantment_limit") ? tag.getInt("enchantment_limit") : 999;
        double currentTotalEnchantmentLevel = 0.0;
        if (itemstack.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                currentTotalEnchantmentLevel += entry.getValue();
            }
        }
        if (currentTotalEnchantmentLevel + level > enchLimit)
            ci.cancel();
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$itemStack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            vminus$itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(capability -> {
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
        if (vminus$itemStack.hasTag() && vminus$itemStack.getTag().contains("reinforcement")) {
            if (vminus$itemStack.getTag().getInt("reinforcement") < vminus$itemStack.getTag().getInt("max_reinforcement")) {
                cir.setReturnValue(true);
            }
        } else if (DurabilityHelper.getDurability(vminus$itemStack) < DurabilityHelper.getDurability(true, vminus$itemStack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "setDamageValue", at = @At(value = "HEAD"), cancellable = true)
    public void setDamageValue(int damage, CallbackInfo ci) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        CompoundTag tag = vminus$itemStack.getTag();
        if (itemData != null && itemData.has("min_damage")) {
            int dealtDamage = vminus$itemStack.getDamageValue();
            int minDamage = VisionValueHandler.isNumberMet(itemData, "min_damage", 0, vminus$itemStack);
            if (dealtDamage < minDamage) {
                vminus$itemStack.getOrCreateTag().putInt("Damage", minDamage);
                ci.cancel();
            }
        }
        if (tag != null && tag.contains("reinforcement")) {
            if (tag.getInt("reinforcement") > 0) {
                int stillDamage = 0;
                tag.putInt("reinforcement", tag.getInt("reinforcement") - damage);
                if (tag.getInt("reinforcement") <= 0) {
                    stillDamage = tag.getInt("reinforcement") * -1;
                    tag.remove("reinforcement");
                    tag.remove("max_reinforcement");
                }
                if (stillDamage > 0)
                    vminus$itemStack.getOrCreateTag().putInt("Damage", vminus$itemStack.getDamageValue() + stillDamage);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "durability");
        if (!propertyMet.isEmpty()) {
            int maxDurability = VisionValueHandler.isNumberMet(itemData, propertyMet, cir.getReturnValue() != null ? cir.getReturnValue() : 0, vminus$itemStack);
            cir.setReturnValue(maxDurability);
        }
    }

    @Inject(method = "isDamageableItem", at = @At("RETURN"), cancellable = true)
    public void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "damageable");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, vminus$itemStack));
        }
    }

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "damageable");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, vminus$itemStack));
        }
    }

    @Inject(method = "isEdible", at = @At("HEAD"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        if (itemData != null && itemData.has("food_properties")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getDrinkingSound", at = @At("HEAD"), cancellable = true)
    private void getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        if (itemData != null && itemData.has("food_properties")) {
            JsonArray foodPropertiesArray = itemData.getAsJsonArray("food_properties");
            for (JsonElement element : foodPropertiesArray) {
                if (element.isJsonObject()) {
                    JsonObject foodProperties = element.getAsJsonObject();
                    if (foodProperties.has("eat_sound")) {
                        String soundName = foodProperties.get("eat_sound").getAsString();
                        SoundEvent eatSound = SoundHelper.getSoundEventFromString(soundName);
                        if (eatSound != null) {
                            cir.setReturnValue(eatSound);
                            cir.cancel();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
    private void hasFoil(CallbackInfoReturnable<Boolean> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "foil");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, vminus$itemStack));
        }
    }

    @Inject(method = "getEatingSound", at = @At("HEAD"), cancellable = true)
    private void getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        if (itemData != null && itemData.has("food_properties")) {
            JsonArray foodPropertiesArray = itemData.getAsJsonArray("food_properties");
            for (JsonElement element : foodPropertiesArray) {
                if (element.isJsonObject()) {
                    JsonObject foodProperties = element.getAsJsonObject();
                    if (foodProperties.has("eat_sound")) {
                        String soundName = foodProperties.get("eat_sound").getAsString();
                        SoundEvent eatSound = SoundHelper.getSoundEventFromString(soundName);
                        if (eatSound != null) {
                            cir.setReturnValue(eatSound);
                            cir.cancel();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDuration(CallbackInfoReturnable<Integer> cir) {
        if (vminus$key == -1)
            vminus$key = VisionHandler.getCacheKey(vminus$itemStack);
        JsonObject itemData = VisionHandler.getVisionData(vminus$itemStack, vminus$key);
        String propertyMet = VisionPropertyNameHandler.propertyMet(itemData, "use_duration");
        if (!propertyMet.isEmpty()) {
            int defaultDuration = 32;
            int calculatedDuration = VisionValueHandler.isNumberMet(itemData, propertyMet, defaultDuration, vminus$itemStack);
            if (calculatedDuration != defaultDuration) {
                cir.setReturnValue(calculatedDuration);
            }
        }
    }

    @Unique
    private int vminus$rgbToColor(float red, float green, float blue) {
        int r = Math.round(red * 255);
        int g = Math.round(green * 255);
        int b = Math.round(blue * 255);
        return (r << 16) | (g << 8) | b;
    }

    @Unique
    private int vminus$interpolateColor(int startColor, int endColor, float ratio) {
        int r1 = (startColor >> 16) & 0xFF;
        int g1 = (startColor >> 8) & 0xFF;
        int b1 = startColor & 0xFF;
        int r2 = (endColor >> 16) & 0xFF;
        int g2 = (endColor >> 8) & 0xFF;
        int b2 = endColor & 0xFF;
        int r = Math.max(0, Math.min(255, (int) (r1 + (r2 - r1) * ratio)));
        int g = Math.max(0, Math.min(255, (int) (g1 + (g2 - g1) * ratio)));
        int b = Math.max(0, Math.min(255, (int) (b1 + (b2 - b1) * ratio)));
        return (r << 16) | (g << 8) | b;
    }
}
