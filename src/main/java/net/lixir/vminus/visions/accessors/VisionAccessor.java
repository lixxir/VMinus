package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.BlockVision;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface VisionAccessor<T> {
    @Nonnull
    T vminus$getVision();

    void vminus$mergeVision(@Nullable T vision);

    void vminus$clearVision();

    void vminus$freezeVision();
}
