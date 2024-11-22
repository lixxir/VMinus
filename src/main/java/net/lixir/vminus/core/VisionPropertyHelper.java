package net.lixir.vminus.core;

import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VisionPropertyHelper {
    private static final HashMap<String, List<String>> PROPERTY_NAME_KEY = new HashMap<String, List<String>>() {{
        put("hides_nametag", Arrays.asList(
                "hide_nametag", "hides_nametag", "hide_nametags", "hides_nametags",
                "hides_name_tag", "hide_name_tag", "hides_name_tags"
        ));
        put("durability", Arrays.asList(
                "durability", "max_damage"
        ));
    }};
    public static String propertyMet(JsonObject visionData, String propertyList) {
        if (visionData != null) {
            for (String key : PROPERTY_NAME_KEY.get(propertyList)) {
                if (visionData.has(key)) {
                    return key;
                }
            }
        }
        return "";
    }
}
