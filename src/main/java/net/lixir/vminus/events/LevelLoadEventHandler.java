package net.lixir.vminus.events;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.ResourceVisionLoader;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LevelLoadEventHandler {
    protected static boolean hasExecuted = false;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onWorldLoad(LevelEvent.Load event) {
        if (hasExecuted) {
            return;
        }

        LevelAccessor world = event.getLevel();
        VisionHandler.clearCaches();

        if (world instanceof ServerLevel serverLevel) {
            hasExecuted = true;
            ResourceVisionLoader.generateItemVisionsFile(serverLevel);
            ResourceVisionLoader.generateBlockVisionsFile(serverLevel);
            ResourceVisionLoader.generateEntityVisionsFile(serverLevel);
            ResourceVisionLoader.generateEffectVisionsFile(serverLevel);
            ResourceVisionLoader.generateEnchantmentVisionsFile(serverLevel);
            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                server.reloadResources(server.getPackRepository().getSelectedIds());
            }
        }
        VisionHandler.loadVisions();
    }
}
