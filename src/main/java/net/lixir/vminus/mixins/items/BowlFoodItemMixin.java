package net.lixir.vminus.mixins.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowlFoodItem.class)
public class BowlFoodItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    public void vminus$finishUsingItem(ItemStack itemStack, Level p_40685_, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (itemStack.getMaxStackSize() == 1)
            return;
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            itemStack.shrink(1);
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }
        }
        cir.setReturnValue(itemStack);
    }
}
