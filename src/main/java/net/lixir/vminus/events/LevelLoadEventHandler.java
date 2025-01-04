package net.lixir.vminus.events;

import com.google.gson.JsonElement;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.visions.ResourceVisionLoader;
import net.lixir.vminus.visions.VisionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber
public class LevelLoadEventHandler {
    protected static volatile boolean hasExecuted = false;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    synchronized public static void onWorldLoad(LevelEvent.Load event) {
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
            /*
            MinecraftServer server = serverLevel.getServer();
            if (server != null) {
                server.reloadResources(server.getPackRepository().getSelectedIds()).thenRun(() -> VMinusMod.LOGGER.info("Resource reload completed.")).exceptionally(throwable -> {
                    VMinusMod.LOGGER.error("Resource reload failed", throwable);
                    return null;
                });
            }

             */
        }
        VisionHandler.loadVisions();
    }
}
