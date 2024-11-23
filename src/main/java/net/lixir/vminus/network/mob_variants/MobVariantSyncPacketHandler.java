package net.lixir.vminus.network.mob_variants;

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
            mc.execute(() -> {
                Entity entity = mc.level.getEntity(packet.getEntityId());
                String variant = packet.getVariant();
                System.out.println(variant);
                if (entity != null) {
                    entity.getPersistentData().putString("variant", variant);
                }
            });
        });
        context.setPacketHandled(true);
    }
}
