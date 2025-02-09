package net.lixir.vminus.core.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class VisionFoodProperties {
    private final Integer nutrition;
    private final Float saturation;
    private final Boolean alwaysEdible;
    private final Boolean isMeat;
    private final SoundEvent eatSound;
    private final SoundEvent burpSound;
    private final List<Pair<MobEffectInstance, Float>> effects;

    public VisionFoodProperties(@Nullable Integer nutrition, @Nullable Float saturation, @Nullable Boolean alwaysEdible, @Nullable Boolean isMeat,
                                @Nullable SoundEvent eatSound, @Nullable SoundEvent burpSound, @Nullable  List<Pair<MobEffectInstance, Float>> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.alwaysEdible = alwaysEdible;
        this.isMeat = isMeat;
        this.eatSound = eatSound;
        this.burpSound = burpSound;
        this.effects = effects;
    }


    public FoodProperties mergeFoodProperties(@Nullable FoodProperties vanilla) {
        FoodProperties.Builder builder = getBuilder(vanilla);

        if (this.effects != null) {
            for (Pair<MobEffectInstance, Float> effect : this.effects) {
                builder.effect(effect.getFirst(), effect.getSecond());
            }
        } else if (vanilla != null) {
            for (Pair<MobEffectInstance, Float> effect : vanilla.getEffects()) {
                builder.effect(effect.getFirst(), effect.getSecond());
            }
        }

        if (Boolean.TRUE.equals(this.alwaysEdible) || (vanilla != null && vanilla.canAlwaysEat())) {
            builder.alwaysEat();
        }

        if (Boolean.TRUE.equals(this.isMeat) || (vanilla != null && vanilla.isMeat())) {
            builder.meat();
        }

        if (vanilla != null && vanilla.isFastFood()) {
            builder.fast();
        }

        return builder.build();
    }

    private FoodProperties.@NotNull Builder getBuilder(FoodProperties vanilla) {
        FoodProperties.Builder builder = new FoodProperties.Builder();

        int nutrition = (this.nutrition != null) ? this.nutrition : (vanilla != null ? vanilla.getNutrition() : 0);
        builder.nutrition(nutrition);

        float saturation = (this.saturation != null) ? this.saturation : (vanilla != null ? vanilla.getSaturationModifier() : 0.0f);
        builder.saturationMod(saturation);
        return builder;
    }

    public SoundEvent getBurpSound() {
        return burpSound;
    }

    public SoundEvent getEatSound() {
        return eatSound;
    }
}
