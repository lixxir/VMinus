package net.lixir.vminus.vision.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;

import javax.annotation.Nullable;
import java.util.List;

public record VisionFoodProperties(Integer nutrition, Float saturation, Boolean alwaysEdible, Boolean isMeat,
                                   SoundEvent eatSound, SoundEvent burpSound,
                                   List<Pair<MobEffectInstance, Float>> effects) {
    public VisionFoodProperties(@Nullable Integer nutrition, @Nullable Float saturation, @Nullable Boolean alwaysEdible, @Nullable Boolean isMeat,
                                @Nullable SoundEvent eatSound, @Nullable SoundEvent burpSound, @Nullable List<Pair<MobEffectInstance, Float>> effects) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.alwaysEdible = alwaysEdible;
        this.isMeat = isMeat;
        this.eatSound = eatSound;
        this.burpSound = burpSound;
        this.effects = effects;
    }

    @Override
    public @Nullable Integer nutrition() {
        return nutrition;
    }

    @Override
    public @Nullable Float saturation() {
        return saturation;
    }

    @Override
    public @Nullable Boolean alwaysEdible() {
        return alwaysEdible;
    }

    @Override
    public @Nullable Boolean isMeat() {
        return isMeat;
    }

    @Override
    public @Nullable List<Pair<MobEffectInstance, Float>> effects() {
        return effects;
    }

    @Override
    public @Nullable SoundEvent burpSound() {
        return burpSound;
    }

    @Override
    public @Nullable SoundEvent eatSound() {
        return eatSound;
    }
}
