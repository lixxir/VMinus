package net.lixir.vminus.mixins.blocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.core.conditions.VisionConditionArguments;
import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.visionable.IBlockVisionable;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.lixir.vminus.util.SoundHelper;
import net.lixir.vminus.core.Visions;
import net.lixir.vminus.core.VisionProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Block.class)
public class BlockRealMixin implements IBlockVisionable {
    @Unique
    private final Block vminus$block = (Block) (Object) this;

    @Unique
    private BlockVision vminus$blockVision = new BlockVision();

    @Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void getSpeedFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().speedFactor.getValue(new VisionConditionArguments.Builder().passBlock(vminus$block).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getFriction", at = @At("RETURN"), cancellable = true)
    private void getFriction(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().friction.getValue(new VisionConditionArguments.Builder().passBlock(vminus$block).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getJumpFactor", at = @At("RETURN"), cancellable = true)
    private void getJumpFactor(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().jumpFactor.getValue(new VisionConditionArguments.Builder().passBlock(vminus$block).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getExplosionResistance", at = @At("RETURN"), cancellable = true)
    private void getExplosionResistance(CallbackInfoReturnable<Float> cir) {
        Float value = vminus$getVision().explosionResistance.getValue(new VisionConditionArguments.Builder().passBlock(vminus$block).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> cir) {
        SoundType value = vminus$getVision().sound.getValue(new VisionConditionArguments.Builder().passBlockState(state).build());
        if (value != null) cir.setReturnValue(value);
    }

    @Override
    public BlockVision vminus$getVision() {
        return vminus$blockVision;
    }

    @Override
    public void vminus$setVision(BlockVision vision) {
        this.vminus$blockVision = vision;
    }
}
