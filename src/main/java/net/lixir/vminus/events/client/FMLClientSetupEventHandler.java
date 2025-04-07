package net.lixir.vminus.events.client;

import net.lixir.vminus.datagen.util.simple.BlockItemDatagen;
import net.lixir.vminus.datagen.util.simple.DatagenObject;
import net.lixir.vminus.datagen.util.simple.DatagenRegistry;
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
        for (List<DatagenObject> value : DatagenRegistry.getValues().values()) {
            if (value instanceof BlockItemDatagen blockItemDatagen) {
                BlockItemRegistryPair blockItemPair = blockItemDatagen.getBlockItemRegistryPair();
                Block block = blockItemPair.block();
                DatagenObject.Type type = blockItemDatagen.getType();
                switch (type) {
                    case FLOWER, LARGE_FLOWER, PLANT, LARGE_PLANT ->   ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
                }
            }
        }
    }
}
