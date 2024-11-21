package net.lixir.vminus.procedures;

import net.lixir.vminus.SetCapePacket;
import net.lixir.vminus.VminusMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SetGhostCapeProcedure {
    public static void execute(Entity entity) {
        if (entity == null || !(entity instanceof Player player))
            return;
        if (!entity.level().isClientSide())
            return;
        UUID playerUUID = player.getUUID();
        VminusMod.PACKET_HANDLER.sendToServer(new SetCapePacket("ghost", playerUUID));
    }
}
