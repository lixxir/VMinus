package net.lixir.vminus.network;

import net.lixir.vminus.RequestFileGenerationPacket;
import net.lixir.vminus.VminusMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientSideWorldLoaded {
    // Requesting all to accumulate all of the stored jsons on the server-side for visions.
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && entity == Minecraft.getInstance().player) {
            //VminusMod.LOGGER.info("Sending RequestFileGenerationPacket to server...");
            VminusMod.PACKET_HANDLER.sendToServer(new RequestFileGenerationPacket());
            //VminusMod.LOGGER.info("Packet sent to server successfully.");
        }
    }
}
