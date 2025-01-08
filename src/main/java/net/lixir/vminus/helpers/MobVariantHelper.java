package net.lixir.vminus.helpers;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.util.VisionValueHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MobVariantHelper {
    public static String setOrGetVariant(Entity entity, JsonObject visionData) {
        String chosenVariant = entity.getPersistentData().getString("variant");
        if (chosenVariant.isEmpty()) {
            List<String> variants = VisionValueHandler.getListOfStrings(visionData, "variants", entity);
            chosenVariant = !variants.isEmpty()
                    ? variants.get(Mth.nextInt(ThreadLocalRandom.current(), 0, variants.size() - 1))
                    : "normal";
            entity.getPersistentData().putString("variant", chosenVariant);
        }
        return chosenVariant;
    }

    public static void setVariant(Entity entity, @Nullable String variant) {
        if (variant != null && !variant.isEmpty()) {
            entity.getPersistentData().putString("variant", variant);
        }
    }

    public static String getVariant(Entity entity) {
        return entity.getPersistentData().getString("variant");
    }
}