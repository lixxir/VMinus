package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.EntityVision;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EntityVisionAccessor extends VisionAccessor<EntityVision> {
    @Override
    @NotNull
    @Nonnull
    EntityVision vminus$getVision();

    @Override
    void vminus$mergeVision(@Nullable EntityVision vision);
}
