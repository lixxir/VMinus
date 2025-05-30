package net.lixir.vminus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class VMinusConfig {

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_ITEMS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> HIDDEN_ITEMS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_ENCHANTMENTS;
    static {

        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("ITEMS");

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