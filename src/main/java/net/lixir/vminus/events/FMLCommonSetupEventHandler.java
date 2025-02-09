package net.lixir.vminus.events;

import net.lixir.vminus.core.VisionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLCommonSetupEventHandler {

    @SubscribeEvent
    public static void vminus$FMLClientSetupEvent(FMLCommonSetupEvent event) {
        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        for (VisionType value : VisionType.values()) {
            configDir = new File(FMLPaths.CONFIGDIR.get().toFile(),  value.getDirectoryName());
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
        }
    }
}
