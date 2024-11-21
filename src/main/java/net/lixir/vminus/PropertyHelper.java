package net.lixir.vminus;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PropertyHelper {
    private static final List<String> hides_nametag_keys = Arrays.asList("hide_nametag", "hides_nametag", "hide_nametags", "hides_nametags", "hides_name_tag", "hide_name_tag", "hides_name_tags");

    public static String propertyMet(JsonObject visionData, ItemStack itemStack, List<String> keys) {
        for (String key : keys) {
            if (visionData.has(key)) {
                return key;
            }
        }
        return "";
    }

    public static String shouldHideNameTag(JsonObject visionData, ItemStack itemStack) {
        return propertyMet(visionData, itemStack, hides_nametag_keys);
    }
}
