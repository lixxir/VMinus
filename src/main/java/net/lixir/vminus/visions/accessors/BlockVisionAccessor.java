package net.lixir.vminus.visions.accessors;

import net.lixir.vminus.visions.BlockVision;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface BlockVisionAccessor extends VisionAccessor<BlockVision> {
    @Override
    @Nonnull
    @NotNull
    BlockVision vminus$getVision();

    @Override
    void vminus$mergeVision(BlockVision vision);
}
