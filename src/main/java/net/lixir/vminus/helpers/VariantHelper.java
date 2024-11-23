package net.lixir.vminus.helpers;

import com.google.gson.JsonObject;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.List;

public class VariantHelper {
    public static String setOrGetVariant(Entity entity, JsonObject visionData) {
        String chosenVariant = entity.getPersistentData().getString("variant");
        if (chosenVariant.isEmpty()) {
            List<String> variants = VisionValueHelper.getListOfStrings(visionData, "variants", entity);
            chosenVariant = !variants.isEmpty()
                    ? variants.get(Mth.nextInt(RandomSource.create(), 0, variants.size() - 1))
                    : "normal";
            entity.getPersistentData().putString("variant", chosenVariant);
        }
        return chosenVariant;
    }

    public static void setVariant(Entity entity, @Nullable String variant) {
        if (variant != null || !variant.isEmpty()) {
            entity.getPersistentData().putString("variant", variant);
        }
    }

    public static String getVariant(Entity entity) {
        String chosenVariant = entity.getPersistentData().getString("variant");
        return chosenVariant;
    }

}