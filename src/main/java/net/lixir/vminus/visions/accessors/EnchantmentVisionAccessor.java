package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.EffectVision;
import net.lixir.vminus.visions.EnchantmentVision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public interface EnchantmentVisionAccessor extends VisionAccessor<EnchantmentVision> {
    @Override
    @NotNull
    @Nonnull
    EnchantmentVision vminus$getVision();

    @Override
    void vminus$mergeVision(@Nullable EnchantmentVision vision);
}
