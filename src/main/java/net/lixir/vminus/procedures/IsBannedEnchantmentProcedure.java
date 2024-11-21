package net.lixir.vminus.procedures;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IsBannedEnchantmentProcedure {
    public static boolean execute(String enchantment) {
        if (enchantment == null)
            return false;
        boolean banned = false;
        File bannedFile = new File("");
        com.google.gson.JsonObject jsonObj = new com.google.gson.JsonObject();
        bannedFile = new File((FMLPaths.GAMEDIR.get().toString() + "/config/vminus/"), File.separator + "banned-enchantments.json");
        banned = false;
        if (bannedFile.exists()) {
            {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(bannedFile));
                    StringBuilder jsonstringbuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonstringbuilder.append(line);
                    }
                    bufferedReader.close();
                    jsonObj = new com.google.gson.Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
                    if (jsonObj.has(enchantment)) {
                        banned = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return banned;
    }
}
