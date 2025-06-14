package net.lixir.vminus.mixins.client;

import net.lixir.vminus.block.VBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Unique
    private Input vMinus$input = (Input) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void detour$tick(boolean p_234118_, float p_234119_, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null && !player.isSpectator() && !player.getAbilities().flying) {
            Level level = player.level();
            BlockPos blockPos = player.blockPosition();
            VBlock vBlock;
            Block blockInside = level.getBlockState(blockPos).getBlock();
            Block blockInsideAbove = level.getBlockState(blockPos.above()).getBlock();
            if (blockInside instanceof VBlock) {
                vBlock = (VBlock) blockInside;
            } else if (blockInsideAbove instanceof VBlock) {
                vBlock = (VBlock) blockInsideAbove;
            } else {
                vBlock = null;
            }
            if (vBlock != null) {
                Input blockInput = vBlock.onPlayerInput(level, player, vMinus$input);
                if (blockInput == null)
                    return;
                vMinus$input.leftImpulse = blockInput.leftImpulse;
                vMinus$input.forwardImpulse = blockInput.forwardImpulse;
                vMinus$input.down = blockInput.down;
                vMinus$input.up = blockInput.up;
                vMinus$input.jumping = blockInput.jumping;
                vMinus$input.right = blockInput.right;
                vMinus$input.left = blockInput.left;
                vMinus$input.shiftKeyDown = blockInput.shiftKeyDown;
                ci.cancel();
            }
        }
    }
}
