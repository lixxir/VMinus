package net.lixir.vminus;

import com.google.gson.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class VMinusConfig {

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue TOOLTIP_REWORK;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_ITEMS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> HIDDEN_ITEMS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_ENCHANTMENTS;
    static {

        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("ITEMS");

        TOOLTIP_REWORK = BUILDER.comment("Defines whether tooltips have a visual rework or not.", "If false, some features regarding tooltip modification may break.")
                .define("tooltipRework", true);
        BANNED_ITEMS = BUILDER.comment("List of items that will not occur during gameplay.", "Specify item IDs to ban.", "May require a restart.")
                .defineList("bannedItems", List.of("minecraft:example"), obj -> obj instanceof String);
        HIDDEN_ITEMS = BUILDER.comment("List of items that will not occur in the creative menus.", "Specify item IDs to hide.", "May require a restart.")
                .defineList("hiddenItems", List.of("minecraft:example"), obj -> obj instanceof String);

        /*
        BUILDER.comment("Enchantments").push("enchantments");

        BANNED_ENCHANTMENTS = BUILDER.comment("List of enchantments that will not occur during gameplay.", "Specify enchantment IDs to ban.")
                .defineList("bannedEnchantments", List.of("minecraft:example"), obj -> obj instanceof String);



         */
        COMMON_CONFIG = BUILDER.build();
    }


}