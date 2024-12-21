package net.lixir.vminus.helpers;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class DurabilityHelper {
    public static int getDurability(ItemStack itemstack) {
        return getDurability(false, itemstack);
    }

    public static int getDurability(boolean max, ItemStack itemstack) {
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        int maxDurability = itemstack.isDamageableItem() ? itemstack.getMaxDamage() : 0;
        int damage = itemstack.isDamageableItem() ? itemstack.getDamageValue() : 0;
        if (itemData != null && itemData.has("fake_durability")) {
            int fakeDurability = VisionValueHandler.isNumberMet(itemData, "fake_durability", maxDurability, itemstack);
            maxDurability += fakeDurability;
        }
        if (itemData != null && itemData.has("fake_durability_multiplier")) {
            int fakeDurability = VisionValueHandler.isNumberMet(itemData, "fake_durability_multiplier", 1, itemstack);
            maxDurability *= fakeDurability;
        }
        int durability = itemstack.isDamageableItem() ? maxDurability - damage : 0;
        if (itemData != null && itemData.has("fake_damage_multiplier")) {
            int fakeDamage = VisionValueHandler.isNumberMet(itemData, "fake_damage_multiplier", 0, itemstack);
            durability -= (fakeDamage * itemstack.getMaxDamage());
        }
        CompoundTag tag = itemstack.getTag();
        if (tag != null && tag.contains("reinforcement")) {
            durability += tag.getInt("reinforcement");
        }
        if (max)
            return maxDurability;
        return durability;
    }
}
