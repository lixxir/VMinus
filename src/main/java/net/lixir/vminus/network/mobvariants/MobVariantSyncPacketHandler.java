package net.lixir.vminus.network.mobvariants;

import net.lixir.vminus.VMinusMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobVariantSyncPacketHandler {
    public static void handle(MobVariantSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                Entity entity = mc.level.getEntity(packet.entityId());
                if (entity != null) {
                    entity.getPersistentData().putString("variant", packet.variant());
                } else {
                    VMinusMod.LOGGER.warn("Entity ID {} not found on client.", packet.entityId());
                }
            } else {
                VMinusMod.LOGGER.error("Client world is null during MobVariantSyncPacket handling!");
            }
        });
        context.setPacketHandled(true);
    }
}
