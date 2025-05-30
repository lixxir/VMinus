package net.lixir.vminus.mixins.blocks;

import net.lixir.vminus.item.trait.ItemTraits;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(at = @At("RETURN"), method = "canEntityWalkOnPowderSnow", cancellable = true)
    private static void canEntityWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            if (ItemTraits.hasTrait(boots, ItemTraits.LIGHTFOOTED.get())) {
                cir.setReturnValue(ItemTraits.getTrait(boots, ItemTraits.LIGHTFOOTED.get()));
                return;
            }

            if (livingEntity instanceof Horse horse) {
                ItemStack horseArmor = horse.getItemBySlot(EquipmentSlot.CHEST);
                if (ItemTraits.hasTrait(horseArmor, ItemTraits.LIGHTFOOTED.get())) {
                    cir.setReturnValue(ItemTraits.getTrait(boots, ItemTraits.LIGHTFOOTED.get()));
                }
            }
        }
    }
}
