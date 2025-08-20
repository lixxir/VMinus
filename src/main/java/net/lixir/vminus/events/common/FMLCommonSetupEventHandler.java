package net.lixir.vminus.events.common;

import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod.EventBusSubscriber
public class FMLCommonSetupEventHandler {
    @SubscribeEvent
    public static void onFMLCommonSetup(final FMLCommonSetupEvent event) {
        createConfigDirectories();
    }

    // Creates directories for the config Visions if they do not already exist.
    private static void createConfigDirectories() {
        File visionDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "visions");
        if (!visionDir.exists()) {
            visionDir.mkdirs();
        }

        File sightDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "sights");
        if (!sightDir.exists()) {
            sightDir.mkdirs();
        }

        File banRecipes = new File(FMLPaths.CONFIGDIR.get().toFile(), "bans/recipes");
        if (!banRecipes.exists()) {
            banRecipes.mkdirs();
        }

        for (VisionType<?> visionType : VisionTypes.getAll()) {
            visionDir = new File(FMLPaths.CONFIGDIR.get().toFile(), visionType.getDirectory());
            if (!visionDir.exists()) {

                visionDir.mkdirs();
            }
        }
    }
}
