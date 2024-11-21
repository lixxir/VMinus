package net.lixir.vminus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconHandler {
    private static final Map<String, String> icons = new HashMap<>();
    private static final Map<String, String> attributeIcons = new HashMap<>();

    static {
        // Formatting
        icons.put("reset", "\u00A7r");
        icons.put("italic", "\u00A7o");
        // Icons
        icons.put("attack_damage", icons.get("reset") + "\uFFF2");
        icons.put("attack_speed", icons.get("reset") + "\uFFF1");
        icons.put("attack_range", icons.get("reset") + "\uFFF3");
        icons.put("armor_toughness", icons.get("reset") + "\uEFF0");
        icons.put("critical_damage", icons.get("reset") + "\uEFE3");
        icons.put("luck", icons.get("reset") + "\uEEE1");
        icons.put("knockback_resistance", icons.get("reset") + "\uEEE0");
        icons.put("jump_strength", icons.get("reset") + "\uEEE3");
        icons.put("health", icons.get("reset") + "\uEEE2");
        icons.put("speed", icons.get("reset") + "\uEFE9");
        icons.put("helmet", icons.get("reset") + "\uEFF1");
        icons.put("chestplate", icons.get("reset") + "\uEFF2");
        icons.put("leggings", icons.get("reset") + "\uEFF3");
        icons.put("boots", icons.get("reset") + "\uEFF4");
        icons.put("horse_armor", icons.get("reset") + "\uEFF5");
        icons.put("shield", icons.get("reset") + "\uEEE8");
        icons.put("sword", icons.get("reset") + "\uFFF4");
        icons.put("pickaxe", icons.get("reset") + "\uFFF5");
        icons.put("axe", icons.get("reset") + "\uFFF6");
        icons.put("shovel", icons.get("reset") + "\uFFF7");
        icons.put("hoe", icons.get("reset") + "\uFFF8");
        icons.put("bow", icons.get("reset") + "\uEFF6");
        icons.put("crossbow", icons.get("reset") + "\uEFF7");
        icons.put("repeater", icons.get("reset") + "\uEFF9");
        icons.put("fishing_rod", icons.get("reset") + "\uEFF8");
        icons.put("shield", icons.get("reset") + "\uEEE8");
        icons.put("anvil", icons.get("reset") + "\uEFE0");
        icons.put("rune", icons.get("reset") + "\uEFE1");
        icons.put("reinforced", icons.get("reset") + "\uEFE2");
        icons.put("shimmered", icons.get("reset") + "\uEEE6");
        icons.put("inspect", icons.get("reset") + "\uEEE4");
        icons.put("inspect_held", icons.get("reset") + "\uEEE5");
        icons.put("inspect_bauble", icons.get("reset") + "\uEEE7");
        icons.put("saturation", icons.get("reset") + "\uEEE9");
        icons.put("hunger_shank", icons.get("reset") + "\uEF0E");
        icons.put("fast_hunger_shank", icons.get("reset") + "\uEF1E");
        icons.put("slow_hunger_shank", icons.get("reset") + "\uEF2E");
        icons.put("eating_duration", icons.get("reset") + "\uEF3E");
        icons.put("effect", icons.get("reset") + "\uEF4E");
        icons.put("bad_effect", icons.get("reset") + "\uEF5E");
        icons.put("fire_protection", icons.get("reset") + "\uEFE4");
        icons.put("blast_protection", icons.get("reset") + "\uEFE5");
        icons.put("magic_protection", icons.get("reset") + "\uEFE6");
        icons.put("fall_protection", icons.get("reset") + "\uEFE7");
        icons.put("mob_detection_range", icons.get("reset") + "\uEF7E");
        icons.put("block_reach", icons.get("reset") + "\uEF6E");
        icons.put("luminance", icons.get("reset") + "\uEF8E");
        icons.put("death_durability", icons.get("reset") + "\uEF01");
        icons.put("health_lost_stat_boost", icons.get("reset") + "\uEF9E");
        // Colors
        icons.put("darkGreenColor", "\u00A72");
        icons.put("blueColor", "\u00A79");
        icons.put("greenColor", "\u00A7a");
        icons.put("redColor", "\u00A7c");
        icons.put("orangeColor", "\u00A76");
        icons.put("grayColor", "\u00A77");
        icons.put("aquaColor", "\u00A7b");
        icons.put("darkGrayColor", "\u00A78");
        icons.put("lightPurpleColor", "\u00A7d");
        // Attribute to Icon Mapping
        attributeIcons.put("vminus:health_lost_stat_boost", "health_lost_stat_boost");
        attributeIcons.put("forge:entity_reach", "attack_range");
        attributeIcons.put("forge:block_reach", "block_reach");
        attributeIcons.put("minecraft:generic.attack_damage", "attack_damage");
        attributeIcons.put("minecraft:generic.attack_speed", "attack_speed");
        attributeIcons.put("minecraft:generic.armor", "chestplate");
        attributeIcons.put("minecraft:generic.jump_strength‌", "jump_strength");
        attributeIcons.put("minecraft:generic.max_health", "health");
        attributeIcons.put("minecraft:generic.armor_toughness", "armor_toughness");
        attributeIcons.put("minecraft:generic.movement_speed", "speed");
        attributeIcons.put("minecraft:generic.luck", "luck");
        attributeIcons.put("minecraft:generic.knockback_resistance", "knockback_resistance");
        attributeIcons.put("vminus:blast_protection", "blast_protection");
        attributeIcons.put("vminus:blunt_protection", "sword");
        attributeIcons.put("vminus:fall_protection", "fall_protection");
        attributeIcons.put("vminus:magic_protection", "magic_protection");
        attributeIcons.put("vminus:fire_protection", "fire_protection");
        attributeIcons.put("vminus:protection", "chestplate");
        attributeIcons.put("vminus:mining_speed", "pickaxe");
        attributeIcons.put("vminus:critical_damage", "critical_damage");
        attributeIcons.put("vminus:mob_detection_range", "mob_detection_range");
    }

    public static String getIcon(String key) {
        return icons.get(key);
    }

    public static String getIconForAttribute(String attributeId) {
        String iconKey = attributeIcons.get(attributeId);
        if (iconKey != null) {
            return icons.get(iconKey);
        }
        return null;
    }

    public static List<String> getIconsList() {
        List<String> iconList = new ArrayList<>();
        for (Map.Entry<String, String> entry : icons.entrySet()) {
            iconList.add(entry.getKey() + ": " + entry.getValue());
        }
        return iconList;
    }
}
