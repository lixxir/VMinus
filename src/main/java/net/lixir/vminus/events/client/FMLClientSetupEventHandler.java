package net.lixir.vminus.events.client;

import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLClientSetupEventHandler {
    @SubscribeEvent
    public static void vminus$FMLClientSetupEvent(FMLClientSetupEvent event) {
        for (UnifiedRegistry unifiedRegistry : UnifiedRegistry.getRegistries()) {
            for (Block block : UnifiedRegistry.fromId(unifiedRegistry.getModId()).getBlocks()) {
                BlockEntryAccessor accessor = (BlockEntryAccessor) block;
                BlockEntry blockEntry = accessor.vminus$getEntry();
                if (blockEntry == null)
                    continue;
                RenderType renderType = blockEntry.getRenderType();
                if (renderType == null)
                    continue;
                ItemBlockRenderTypes.setRenderLayer(block, renderType);
            }
        }
    }
}
