package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.ItemVision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public interface ItemVisionAccessor extends VisionAccessor<ItemVision> {
    @Override
    @Nonnull
    @NotNull
    ItemVision vminus$getVision();

    @Override
    void vminus$mergeVision(@Nullable ItemVision vision);
}
