package net.lixir.vminus.events;

import net.lixir.vminus.visions.ResourceVisionHelper;
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
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onWorldLoad(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();
        VisionHandler.clearCaches();
        if (world instanceof ServerLevel _serverlevel) {
            ResourceVisionHelper.generateItemVisionsFile(_serverlevel);
            ResourceVisionHelper.generateBlockVisionsFile(_serverlevel);
            ResourceVisionHelper.generateEntityVisionsFile(_serverlevel);
            ResourceVisionHelper.generateEffectVisionsFile(_serverlevel);
            ResourceVisionHelper.generateEnchantmentVisionsFile(_serverlevel);
            // Required for vision recipe changes.
            MinecraftServer server = _serverlevel.getServer();
            if (server != null) {
                server.reloadResources(server.getPackRepository().getSelectedIds());
            }
        }
        VisionHandler.loadVisions();
    }
}
