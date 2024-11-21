package net.lixir.vminus.procedures;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Mod.EventBusSubscriber
public class BannedEnchantmentsProcedure {
    @SubscribeEvent
    public static void onWorldLoad(net.minecraftforge.event.level.LevelEvent.Load event) {
        execute(event);
    }

    public static void execute() {
        execute(null);
    }

    private static void execute(@Nullable Event event) {
        File theFile = new File("");
        com.google.gson.JsonObject jsonObj = new com.google.gson.JsonObject();
        theFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/vminus/"), File.separator + "banned-enchantments.json");
        if (!theFile.exists()) {
            try {
                theFile.getParentFile().mkdirs();
                theFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            jsonObj.addProperty("mod_id:enchantment_id", true);
            if (ModList.get().isLoaded("abyssmal")) {
                jsonObj.addProperty("minecraft:mending", true);
                jsonObj.addProperty("minecraft:fortune", true);
                jsonObj.addProperty("minecraft:efficiency", true);
                jsonObj.addProperty("minecraft:bane_of_arthropods", true);
                jsonObj.addProperty("minecraft:smite", true);
                jsonObj.addProperty("minecraft:sharpness", true);
                jsonObj.addProperty("minecraft:looting", true);
                jsonObj.addProperty("minecraft:power", true);
                jsonObj.addProperty("minecraft:infinity", true);
                jsonObj.addProperty("minecraft:aqua_affinity", true);
                jsonObj.addProperty("minecraft:respiration", true);
                jsonObj.addProperty("minecraft:depth_strider", true);
                jsonObj.addProperty("minecraft:protection", true);
                jsonObj.addProperty("minecraft:blast_protection", true);
                jsonObj.addProperty("minecraft:fire_protection", true);
                jsonObj.addProperty("minecraft:projectile_protection", true);
                jsonObj.addProperty("minecraft:feather_falling", true);
                jsonObj.addProperty("minecraft:depth_strider", true);
                jsonObj.addProperty("minecraft:soul_speed", true);
                jsonObj.addProperty("minecraft:impaling", true);
                jsonObj.addProperty("minecraft:luck_of_the_sea", true);
                jsonObj.addProperty("minecraft:lure", true);
                jsonObj.addProperty("minecraft:soul_speed", true);
                jsonObj.addProperty("minecraft:unbreaking", true);
                jsonObj.addProperty("minecraft:quick_charge", true);
                jsonObj.addProperty("abyssmal:temporary", true);
                jsonObj.addProperty("minecraft:piercing", true);
                jsonObj.addProperty("minecraft:swift_sneak", true);
            }
            {
                com.google.gson.Gson mainGSONBuilderVariable = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
                try {
                    FileWriter fileWriter = new FileWriter(theFile);
                    fileWriter.write(mainGSONBuilderVariable.toJson(jsonObj));
                    fileWriter.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
