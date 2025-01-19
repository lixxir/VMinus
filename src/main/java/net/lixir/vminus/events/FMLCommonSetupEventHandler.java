package net.lixir.vminus.events;

import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLCommonSetupEventHandler {
    @SubscribeEvent
    public static void vminus$FMLClientSetupEvent(FMLCommonSetupEvent event) {

        File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus/item_visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus/block_visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus/entity_visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus/effect_visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "vminus/enchantment_visions");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }
}
