package net.lixir.vminus.events.entity.player;

import net.lixir.vminus.item.IEquipmentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class PlayerTickEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        Player player = event.player;
        Level level = player.level();
        CompoundTag nbt = player.getPersistentData();
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        if (player.isSpectator() || player.getAbilities().flying)
            return;
        if (leggings.getItem() instanceof IEquipmentItem IEquipmentItem) {
            if (player.isCrouching()) {
                int crouchTicks = nbt.getInt("CrouchTicks");
                if (crouchTicks == 0)
                    IEquipmentItem.onCrouch(level, player, leggings);
                crouchTicks++;
                nbt.putInt("CrouchTicks", crouchTicks);

                IEquipmentItem.onCrouchTick(level, player, leggings, crouchTicks);
            } else {
                IEquipmentItem.OnUnCrouch(level, player, leggings, nbt.contains("CrouchTicks") ? nbt.getInt("CrouchTicks") : 0);
                if (nbt.contains("CrouchTicks"))
                    nbt.putInt("CrouchTicks", 0);
            }
        }
    }
}
