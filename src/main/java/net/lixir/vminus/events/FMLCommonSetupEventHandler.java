package net.lixir.vminus.events;

import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
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
        createConfigDirectories();
    }

    // Creates directories for the config Visions if they do not already exist.
    private static void createConfigDirectories() {
        File visionConfigDirectory = new File(FMLPaths.CONFIGDIR.get().toFile(), "visions");
        if (!visionConfigDirectory.exists()) {
            visionConfigDirectory.mkdirs();
        }

        File sightConfigDirectory = new File(FMLPaths.CONFIGDIR.get().toFile(), "sights");
        if (!sightConfigDirectory.exists()) {
            sightConfigDirectory.mkdirs();
        }

        for (VisionType visionType : VisionTypes.getAll()) {
            visionConfigDirectory = new File(FMLPaths.CONFIGDIR.get().toFile(), visionType.directory());
            if (!visionConfigDirectory.exists()) {

                visionConfigDirectory.mkdirs();
            }
        }
    }
}
