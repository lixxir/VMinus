package net.lixir.vminus.util;

import com.mojang.datafixers.util.Pair;
import net.lixir.vminus.vision.util.VisionFoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FoodPropertiesUtil {
    public static FoodProperties merge(@Nullable FoodProperties vanilla, @NotNull VisionFoodProperties vision) {
        FoodProperties.Builder builder = getBuilder(vanilla, vision);

        if (vision.effects() != null && !vision.effects().isEmpty()) {
            for (Pair<MobEffectInstance, Float> effect : vision.effects()) {
                builder.effect(effect.getFirst(), effect.getSecond());
            }
        } else if (vanilla != null) {
            vanilla.getEffects().forEach(pair -> builder.effect(pair.getFirst(), pair.getSecond()));
        }

        if (Boolean.TRUE.equals(vision.alwaysEdible()) || (vanilla != null && vanilla.canAlwaysEat())) {
            builder.alwaysEat();
        }

        if (Boolean.TRUE.equals(vision.isMeat()) || (vanilla != null && vanilla.isMeat())) {
            builder.meat();
        }

        if (vanilla != null && vanilla.isFastFood()) {
            builder.fast();
        }

        return builder.build();
    }

    private static FoodProperties.@NotNull Builder getBuilder(@Nullable FoodProperties vanilla, @NotNull VisionFoodProperties vision) {
        FoodProperties.Builder builder = new FoodProperties.Builder();

        int nutrition = vision.nutrition() != null ? vision.nutrition() : (vanilla != null ? vanilla.getNutrition() : 0);
        builder.nutrition(nutrition);

        float saturation = vision.saturation() != null ? vision.saturation() : (vanilla != null ? vanilla.getSaturationModifier() : 0.0f);
        builder.saturationMod(saturation);

        return builder;
    }
}
