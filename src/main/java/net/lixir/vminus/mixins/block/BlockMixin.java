package net.lixir.vminus.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.vision.*;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.lixir.vminus.vision.util.VisionUtils;
import net.lixir.vminus.vision.values.conditions.VisionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin implements VisionDuck, BlockDefinitionDuck {
    @Unique
    private final Block vMinus$self = (Block) (Object) this;

    @Unique
    private BlockDefinition vMinus$blockDefinition = null;

    @Unique
    private ResourceLocation vMinus$visionId = null;

    @ModifyReturnValue(method = "getCloneItemStack", at = @At("RETURN"))
    private ItemStack vMinus$getCloneItemStack(ItemStack original) {
        ItemStack replacement = ItemReplacement.resolve(original);
        if (!replacement.isEmpty()) {
            return replacement;
        }
        return original;
    }

    @ModifyReturnValue(method = "getSpeedFactor", at = @At("RETURN"))
    private float vMinus$getSpeedFactor(float original) {
        return Vision.getValue(vMinus$self, VisionProperties.Blocks.SPEED_FACTOR, original);
    }

    @ModifyReturnValue(method = "getFriction", at = @At("RETURN"))
    private float vMinus$getFriction(float original) {
        return Vision.getValue(vMinus$self, VisionProperties.Blocks.FRICTION, original);
    }

    @ModifyReturnValue(method = "getJumpFactor", at = @At("RETURN"))
    private float vMinus$getJumpFactor(float original) {
        return Vision.getValue(vMinus$self, VisionProperties.Blocks.JUMP_FACTOR, original);
    }

    @ModifyReturnValue(method = "getExplosionResistance", at = @At("RETURN"))
    private float vMinus$getExplosionResistance(float original) {
        return Vision.getValue(vMinus$self, VisionProperties.Blocks.BLAST_RESISTANCE, original);
    }

    @SuppressWarnings("deprecation")
    @ModifyReturnValue(method = "getSoundType", at = @At("RETURN"))
    private SoundType vMinus$getSoundType(SoundType original) {
        SoundType override = VisionUtils.getOverrideValue(this, VisionProperties.Blocks.SOUND, new VisionContext(vMinus$self));

        if (override != null) {
            float volume = override.getVolume() != 0.0F ? override.getVolume() : original.getVolume();
            float pitch = override.getPitch() != 0.0F ? override.getPitch() : original.getPitch();
            return new SoundType(volume, pitch, override.getBreakSound(), override.getStepSound(), override.getPlaceSound(), override.getHitSound(), override.getFallSound());
        }
        return original;
    }


    @Override
    public void vMinus$setDefinition(@Nullable BlockDefinition blockDefinition) {
        this.vMinus$blockDefinition = blockDefinition;
    }

    @Nullable
    @Override
    public BlockDefinition vMinus$getDefinition() {
        return vMinus$blockDefinition;
    }

    @Override
    public void vMinus$setVisionId(ResourceLocation id) {
        this.vMinus$visionId = id;
    }

    @Override
    public @Nullable ResourceLocation vMinus$getVisionId() {
        return vMinus$visionId;
    }

    @Override
    public @NonNull VisionType<?> vMinus$getVisionType() {
        return VisionTypes.BLOCK;
    }
}
