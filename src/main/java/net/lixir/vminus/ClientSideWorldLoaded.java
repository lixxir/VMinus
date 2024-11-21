package net.lixir.vminus;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientSideWorldLoaded {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        execute(event.getEntity());
    }

    public static void execute(Entity entity) {
        if (entity instanceof Player && entity == Minecraft.getInstance().player) {
            VminusMod.LOGGER.info("Sending RequestFileGenerationPacket to server...");
            VminusMod.PACKET_HANDLER.sendToServer(new RequestFileGenerationPacket());
            VminusMod.LOGGER.info("Packet sent to server successfully.");
        }
    }
}
