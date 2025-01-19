package net.lixir.vminus.events.client;

import net.lixir.vminus.registry.util.BlockSet;
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
        for (BlockSet blockSet : BlockSet.BLOCK_SETS) {
            if (blockSet.hasLeaves()) {
                Block block = blockSet.getLeavesBlock();
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
            }
        }

    }
}
