package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.EffectVision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public interface EffectVisionAccessor extends VisionAccessor<EffectVision> {
    @Override
    @NotNull
    @Nonnull
    EffectVision vminus$getVision();

    @Override
    void vminus$mergeVision(@Nullable EffectVision vision);
}
