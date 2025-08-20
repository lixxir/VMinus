package net.lixir.vminus.mixins.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowlFoodItem.class)
public abstract class BowlFoodItemMixin extends Item {
    public BowlFoodItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Shadow
    public abstract @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_40684_, @NotNull Level p_40685_, @NotNull LivingEntity p_40686_);

    // Patches to make stacks work above 1
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void vMinus$finishUsingItem(@NotNull ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (itemStack.getCount() == 1)
            return;
        itemStack = super.finishUsingItem(itemStack, level, entity);
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack bowl = new ItemStack(Items.BOWL);
            if (!player.getInventory().add(bowl)) {
                player.drop(bowl, false);
            }
        }
        cir.setReturnValue(itemStack);
    }
}
