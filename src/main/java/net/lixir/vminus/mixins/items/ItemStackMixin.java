package net.lixir.vminus.mixins.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.SoundHelper;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.helpers.DurabilityHelper;
import net.lixir.vminus.visions.VisionValueHandler;
import net.lixir.vminus.visions.util.EnchantmentVisionHelper;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionPropertyHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private final ItemStack itemstack = (ItemStack) (Object) this;
    @Shadow
    @Nullable
    private CompoundTag tag;
    @Shadow
    @Nullable
    private Entity entityRepresentation;

    @Shadow
    protected static Collection<Component> expandBlockState(String string) {
        return null;
    }

    private static String formatDoubleSafely(Double value) {
        if (value == null) {
            return null;
        }
        NumberFormat formatter = new DecimalFormat("#.#");
        return formatter.format(value);
    }

    @Shadow
    public abstract Item getItem();

    @Shadow
    protected abstract int getHideFlags();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getBarColor();

    @Shadow
    public abstract Rarity getRarity();

    @Shadow
    public abstract boolean isDamageableItem();

    @Inject(method = "hurt", at = @At(value = "RETURN"), cancellable = true)
    public void hurt(int i, RandomSource random, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            String replaceId = VisionValueHandler.getFirstValidString(null, "break_replacement", itemstack);
            if (replaceId != null && !replaceId.isEmpty()) {
                final ItemStack findItem = itemstack;
                CompoundTag tag = findItem.getOrCreateTag();
                ItemStack replacementStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(replaceId)));
                int slotIndex = -1;
                slotIndex = player.getInventory().findSlotMatchingItem(findItem);
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
                    if (VisionValueHandler.isBooleanMet(null, "break_replacement", itemstack, "carry_nbt")) {
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
                    itemstack.shrink(1);
                }
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        ItemStack itemstack = ((ItemStack) (Object) this);
        if (itemstack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
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
        if (itemstack.hasTag() && itemstack.getTag().contains("reinforcement")) {
            float durabilityRatio = (float) itemstack.getTag().getInt("reinforcement") / (float) itemstack.getTag().getInt("max_reinforcement");
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        } else if (itemstack.isDamageableItem()) {
            float durabilityRatio = (float) DurabilityHelper.getDurability(itemstack) / (float) DurabilityHelper.getDurability(true, itemstack);
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            cir.setReturnValue(Math.min(barWidth, 13));
        }
    }

    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("bar")) {
            int startColor = 4384126;
            int endColor = 2186818;
            try {
                String startColorString = VisionValueHandler.getFirstValidString(itemData, "bar", itemstack, "start_color");
                if (startColorString != null)
                    startColor = Integer.decode(startColorString.trim());
            } catch (NumberFormatException e) {
                VMinusMod.LOGGER.error("Invalid start_color format: " + itemData.get("start_color").getAsString());
            }
            try {
                String endColorString = VisionValueHandler.getFirstValidString(itemData, "bar", itemstack, "end_color");
                if (endColorString != null)
                    endColor = Integer.decode(endColorString.trim());
            } catch (NumberFormatException e) {
                VMinusMod.LOGGER.error("Invalid end_color format: " + itemData.get("end_color").getAsString());
            }
            float durabilityRatio = (float) DurabilityHelper.getDurability(itemstack) / (float) DurabilityHelper.getDurability(true, itemstack);
            int transitionColor = interpolateColor(endColor, startColor, durabilityRatio);
            cir.setReturnValue(transitionColor);
            cir.cancel();
        } else {
            if (itemstack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
                itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
                    int numberOfSlots = capability.getSlots();
                    int totalItems = 0;
                    int maxCapacity = 0;
                    for (int i = 0; i < numberOfSlots; i++) {
                        ItemStack itemStackInSlot = capability.getStackInSlot(i);
                        totalItems += itemStackInSlot.getCount();
                        maxCapacity += itemStackInSlot.getMaxStackSize();
                    }
                    float fullness = maxCapacity > 0 ? (float) totalItems / maxCapacity : 0;
                    int containerItemColor = rgbToColor(0.4F, 0.4F, 1.0F);
                    cir.setReturnValue(containerItemColor);
                });
            }
            if (itemstack.isDamageableItem()) {
                if (itemstack.getTag().getBoolean("broken")) {
                    cir.setReturnValue(Mth.hsvToRgb(0.01F, 0.0F, 0.35F));
                } else if (itemstack.hasTag() && itemstack.getTag().contains("reinforcement")) {
                    int startColor = 0x55FFFF;
                    int endColor = 0x22a53f;
                    float durabilityRatio = (float) itemstack.getTag().getInt("reinforcement") / (float) itemstack.getTag().getInt("max_reinforcement");
                    int transitionColor = interpolateColor(endColor, startColor, durabilityRatio);
                    cir.setReturnValue(transitionColor);
                } else {
                    int startColor;
                    int endColor;
                    float durabilityRatio = (float) DurabilityHelper.getDurability(itemstack) / (float) DurabilityHelper.getDurability(true, itemstack);
                    if (itemstack.getTag().getBoolean("death_durability")) {
                        startColor = 0xFF00FF;
                        endColor = 0x550055;
                    } else {
                        startColor = 0x69fc2a;
                        endColor = 0xe22626;
                    }
                    int transitionColor = interpolateColor(endColor, startColor, durabilityRatio);
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
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemstack.is(ItemTags.create(new ResourceLocation("vminus:containers")))) {
            itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(capability -> {
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
        if (itemstack.hasTag() && itemstack.getTag().contains("reinforcement")) {
            if (itemstack.getTag().getInt("reinforcement") < itemstack.getTag().getInt("max_reinforcement")) {
                cir.setReturnValue(true);
            }
        } else if (DurabilityHelper.getDurability(itemstack) < DurabilityHelper.getDurability(true, itemstack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "setDamageValue", at = @At(value = "HEAD"), cancellable = true)
    public void setDamageValue(int damage, CallbackInfo ci) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        CompoundTag tag = itemstack.getTag();
        if (itemData != null && itemData.has("min_damage")) {
            int dealtDamage = itemstack.getDamageValue();
            int minDamage = VisionValueHandler.isNumberMet(itemData, "min_damage", 0, itemstack);
            if (dealtDamage < minDamage) {
                itemstack.getOrCreateTag().putInt("Damage", minDamage);
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
                    itemstack.getOrCreateTag().putInt("Damage", itemstack.getDamageValue() + stillDamage);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "durability");
        if (!propertyMet.isEmpty()) {
            int maxDurability = VisionValueHandler.isNumberMet(itemData, propertyMet, cir.getReturnValue() != null ? cir.getReturnValue() : 0, itemstack);
            cir.setReturnValue(maxDurability);
        }
    }

    @Inject(method = "isDamageableItem", at = @At("RETURN"), cancellable = true)
    public void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "damageable");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, itemstack));
        }
    }

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void isEnchantable(CallbackInfoReturnable<Boolean> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "damageable");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, itemstack));
        }
    }

    @Inject(method = "isEdible", at = @At("HEAD"), cancellable = true)
    private void isEdible(CallbackInfoReturnable<Boolean> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData != null && itemData.has("food_properties")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getDrinkingSound", at = @At("HEAD"), cancellable = true)
    private void getDrinkingSound(CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
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
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "foil");
        if (!propertyMet.isEmpty()) {
            cir.setReturnValue(VisionValueHandler.isBooleanMet(itemData, propertyMet, itemstack));
        }
    }

    @Inject(method = "getEatingSound", at = @At("HEAD"), cancellable = true)
    private void getEatingSound(CallbackInfoReturnable<SoundEvent> cir) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
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
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        String propertyMet = VisionPropertyHandler.propertyMet(itemData, "use_duration");
        if (!propertyMet.isEmpty()) {
            int defaultDuration = 32;
            int calculatedDuration = VisionValueHandler.isNumberMet(itemData, propertyMet, defaultDuration, itemstack);
            if (calculatedDuration != defaultDuration) {
                cir.setReturnValue(calculatedDuration);
            }
        }
    }

    private int rgbToColor(float red, float green, float blue) {
        int r = Math.round(red * 255);
        int g = Math.round(green * 255);
        int b = Math.round(blue * 255);
        return (r << 16) | (g << 8) | b;
    }

    private int interpolateColor(int startColor, int endColor, float ratio) {
        int r1 = (startColor >> 16) & 0xFF;
        int g1 = (startColor >> 8) & 0xFF;
        int b1 = startColor & 0xFF;
        int r2 = (endColor >> 16) & 0xFF;
        int g2 = (endColor >> 8) & 0xFF;
        int b2 = endColor & 0xFF;
        int r = Math.max(0, Math.min(255, (int) (r1 + (r2 - r1) * ratio)));
        int g = Math.max(0, Math.min(255, (int) (g1 + (g2 - g1) * ratio)));
        int b = Math.max(0, Math.min(255, (int) (b1 + (b2 - b1) * ratio)));
        int transitionColor = (r << 16) | (g << 8) | b;
        return transitionColor;
    }
}
