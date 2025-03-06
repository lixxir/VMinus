package net.lixir.vminus.events.client;

import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLClientSetupEventHandler {
    @SubscribeEvent
    public static void vminus$FMLClientSetupEvent(FMLClientSetupEvent event) {
        for (List<BlockSetup> blockSetupList : SetupRegistries.BLOCKS.getValues().values()) {
            for (BlockSetup blockSetup : blockSetupList) {
                BlockItemRegistryPair blockItemPair = blockSetup.getBlockItemPair();
                Block block = blockItemPair.block();
                RenderType renderType = blockSetup.getRenderType();
                if (renderType != null) {
                    ItemBlockRenderTypes.setRenderLayer(block, renderType);
                }
            }
        }
    }
}
