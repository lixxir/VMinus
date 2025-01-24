package net.lixir.vminus.vision.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.vision.Vision;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentVisionHelper {
    public static boolean isBanned(Enchantment enchantment) {
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && visionData.has("banned")) {
            return VisionValueHandler.isBooleanMet(visionData, "banned");
        }
        return false;
    }

    public static int getMinLevel(Enchantment enchantment, int defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && visionData.has("min_level")) {
            return VisionValueHandler.isNumberMet(visionData, "min_level", defaultValue);
        }
        return defaultValue;
    }

    public static int getMaxLevel(Enchantment enchantment, int defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && visionData.has("max_level")) {
            return VisionValueHandler.isNumberMet(visionData, "max_level", defaultValue);
        }
        return defaultValue;
    }

    public static boolean isTreasure(Enchantment enchantment, @Nullable boolean defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (isBanned(enchantment))
            return false;
        if (visionData != null && visionData.has("treasure")) {
            return VisionValueHandler.isBooleanMet(visionData, "treasure");
        }
        return defaultValue;
    }

    public static boolean isTradeable(Enchantment enchantment, @Nullable boolean defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (isBanned(enchantment))
            return false;
        if (visionData != null && visionData.has("tradeable")) {
            return VisionValueHandler.isBooleanMet(visionData, "tradeable");
        }
        return defaultValue;
    }

    public static boolean isDiscoverable(Enchantment enchantment, @Nullable boolean defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (isBanned(enchantment))
            return false;
        if (visionData != null && visionData.has("discoverable")) {
            return VisionValueHandler.isBooleanMet(visionData, "discoverable");
        }
        return defaultValue;
    }

    public static boolean isCurse(Enchantment enchantment, @Nullable boolean defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && visionData.has("curse")) {
            return VisionValueHandler.isBooleanMet(visionData, "curse");
        }
        return defaultValue;
    }

    public static boolean canEnchant(Enchantment enchantment, ItemStack itemstack, @Nullable boolean defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (isBanned(enchantment))
            return false;
        if (visionData == null) {
            return defaultValue;
        }
        if (visionData.has("exclusive_enchantable")) {
            boolean exclusive = VisionValueHandler.isBooleanMet(visionData, "exclusive_enchantable");
            if (exclusive) {
                if (visionData.has("enchantable")) {
                    boolean matches = VisionValueHandler.matchItemList(visionData, "enchantable", itemstack);
                    if (!matches) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        if (visionData.has("enchantable")) {
            boolean matches = VisionValueHandler.matchItemList(visionData, "enchantable", itemstack);
            if (matches) {
                return true;
            }
        }
        return defaultValue;
    }

    public static boolean isCompatible(Enchantment enchantment, Enchantment otherEnchantment, @Nullable boolean defaultValue) {
        if (isBanned(enchantment) || isBanned(otherEnchantment))
            return false;
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && (visionData.has("compatible") || visionData.has("incompatible")))
            return VisionValueHandler.EnchantmentCompatibleWith(visionData, enchantment, otherEnchantment);
        return defaultValue;
    }

    public static Enchantment.Rarity getRarity(Enchantment enchantment, @Nullable Enchantment.Rarity defaultValue) {
        JsonObject visionData = Vision.getData(enchantment);
        if (visionData != null && visionData.has("rarity")) {
            String rarity = VisionValueHandler.getFirstValidString(visionData, "rarity").toLowerCase();
            if (rarity != null && !rarity.isEmpty()) {
                switch (rarity) {
                    case "uncommon":
                        return Enchantment.Rarity.UNCOMMON;
                    case "rare":
                        return Enchantment.Rarity.RARE;
                    case "very_rare":
                        return Enchantment.Rarity.VERY_RARE;
                    default:
                        return Enchantment.Rarity.COMMON;
                }
            }
        }
        return defaultValue;
    }
}
