package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.CreativeTabVision;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CreativeTabVisionAccessor extends VisionAccessor<CreativeTabVision> {
    @Override
    @NotNull
    @Nonnull
    CreativeTabVision vminus$getVision();

    @Override
    void vminus$mergeVision(@Nullable CreativeTabVision vision);
}
