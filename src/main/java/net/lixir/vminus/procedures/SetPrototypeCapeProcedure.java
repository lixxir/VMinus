package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.capes.SetCapePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class SetPrototypeCapeProcedure {
    public static void execute(Entity entity) {
        if (entity == null || !(entity instanceof Player player))
            return;
        if (!entity.level().isClientSide())
            return;
        UUID playerUUID = player.getUUID();
        VMinusMod.PACKET_HANDLER.sendToServer(new SetCapePacket("protoype", playerUUID));
    }
}
