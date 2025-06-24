package net.lixir.vminus.mixins.block;

import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviorMixin implements VisionDuck {

    @Unique
    private final BlockBehaviour vMinus$self = (BlockBehaviour) (Object) this;

}
