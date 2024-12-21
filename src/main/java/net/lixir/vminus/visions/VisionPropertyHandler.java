package net.lixir.vminus.visions;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VisionPropertyHandler {
    private static final HashMap<String, List<String>> PROPERTY_NAME_KEY = new HashMap<String, List<String>>() {{
        put("hides_nametag", Arrays.asList(
                "hide_nametag", "hides_nametag", "hide_nametags", "hides_nametags",
                "hides_name_tag", "hide_name_tag", "hides_name_tags"
        ));
        put("durability", Arrays.asList(
                "durability", "max_damage"
        ));
        put("damageable", Arrays.asList(
                "damageable", "is_damageable", "can_be_damaged"
        ));
        put("enchantable", Arrays.asList(
                "enchantable", "is_enchantable", "can_be_enchanted"
        ));
        put("foil", Arrays.asList(
                "foil", "enchantment_glint", "glint", "enchanting_foil", "enchantment_foil", "enchanting_glint"
        ));
        put("use_duration", Arrays.asList(
                "use_duration", "use_time", "use_speed"
        ));
        put("stack_size", Arrays.asList(
                "stack_size", "stacks_to", "max_stack", "max_stack_size"
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
