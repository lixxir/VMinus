package net.lixir.vminus.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IEquipmentItem {
    default void onEntityJump(Level level, LivingEntity entity, ItemStack boots, boolean onGround) {

    }

    default void OnUnCrouch(Level level, Player player, ItemStack leggings, int tickDuration) {

    }

    default void onCrouch(Level level, Player player, ItemStack leggings) {

    }

    default void onCrouchTick(Level level, Player player, ItemStack leggings, int totalTicks) {

    }

    default void onEquipmentTick(Level level, LivingEntity entity, ItemStack armorPiece) {

    }
}


