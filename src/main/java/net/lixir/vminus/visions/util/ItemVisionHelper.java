package net.lixir.vminus.visions.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.world.item.ItemStack;
public class ItemVisionHelper {
    public static boolean isBanned(ItemStack itemstack){
        JsonObject itemData = VisionHandler.getVisionData(itemstack);
        if (itemData.has("banned") && VisionValueHandler.isBooleanMet(itemData, "banned", itemstack)) {
            return true;
        }
        return false;
    }
}
