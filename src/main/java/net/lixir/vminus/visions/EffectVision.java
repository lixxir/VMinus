package net.lixir.vminus.visions;

import net.lixir.vminus.visions.accessors.EffectVisionAccessor;
import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.resources.codec.*;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class EffectVision extends Vision {
    public static final EffectVision EMPTY = new EffectVision();

    public final VisionProperty<Boolean> ban;
    public final VisionProperty<MobEffectCategory> category;
    public final VisionProperty<Integer> color;
    public final VisionProperty<ParticleType<?>> particle;

    public EffectVision() {
        ban = create("ban", VisionCodecs.booleanCodec());
        category = create("category", new VisionEnumCodec<>(MobEffectCategory.class));
        color = create("color", VisionCodecs.hexCodec());
        particle = create("particle", new VisionParticleCodec());
    }

    public static @NotNull EffectVision of(MobEffect effect) {
        if (effect instanceof EffectVisionAccessor effectVisionAccessor)
            return effectVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.EFFECT.getListName();
    }
}
