package net.lixir.vminus.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.vision.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin implements VisionDuck {

    @Shadow protected abstract Block asBlock();

    @ModifyReturnValue(method = "getLootTable", at = @At("RETURN"))
    private ResourceLocation vMinus$getLootTable(ResourceLocation original) {
        return Vision.getValue(asBlock(), VisionProperties.Blocks.LOOT_TABLE, original);
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.BLOCK;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return ((VisionDuck) asBlock()).vMinus$getVisionId();
    }
}
