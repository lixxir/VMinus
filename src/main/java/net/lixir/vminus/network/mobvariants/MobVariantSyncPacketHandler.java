package net.lixir.vminus.network.mobvariants;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobVariantSyncPacketHandler {
    public static void handle(MobVariantSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        System.out.println("Mob variant sync packet being handled.");
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.execute(() -> {
                int entityId = packet.entityId();
                Entity entity = mc.level.getEntity(entityId);
                String variant = packet.variant();
                System.out.println(variant);
                System.out.println(entity);
                System.out.println(entityId);
                if (entity != null) {
                    entity.getPersistentData().putString("variant", variant);
                }
            });
        });
        context.setPacketHandled(true);
    }
}
