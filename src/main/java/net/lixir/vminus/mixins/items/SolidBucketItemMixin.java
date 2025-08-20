package net.lixir.vminus.mixins.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SolidBucketItem.class)
public abstract class SolidBucketItemMixin extends BlockItem {
    public SolidBucketItemMixin(Block block, Properties properties) {
        super(block, properties);
    }

    // Patches to make stacks work above 1
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void vMinus$useOn(@NotNull UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (itemStack.getCount() == 1)
            return;
        InteractionResult interactionResult = super.useOn(context);
        if (interactionResult.consumesAction() && player != null  && !player.getAbilities().instabuild) {
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            if (!player.getInventory().add(emptyBucket)) {
                player.drop(emptyBucket, false);
            }
        }
        cir.setReturnValue(interactionResult);
        cir.cancel();
    }
}
