package net.lixir.vminus.events.client;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.api.rendertype.RenderTypeKey;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FMLClientSetupEventHandler {
    @SubscribeEvent
    public static void vminus$FMLClientSetupEvent(FMLClientSetupEvent event) {
        for (VRegistry vRegistry : VRegistry.getRegistries()) {
            for (Block block : VRegistry.fromId(vRegistry.getModId()).getBlocks()) {
                BlockDefinition blockDefinition = BlockDefinition.of(block);
                RenderTypeKey renderTypeKey = blockDefinition.getRenderTypeKey();
                RenderType renderType = getRenderType(renderTypeKey);
                if (renderTypeKey.isUnset())
                    continue;
                VMinus.LOGGER.info("Render Type({}) registered for {}", renderTypeKey, block);
                ItemBlockRenderTypes.setRenderLayer(block, renderType);
            }
        }
    }

    private static @NotNull RenderType getRenderType(RenderTypeKey renderTypeKey) {
        String rawRenderType = renderTypeKey.key();
        return switch (rawRenderType) {
            case "cutout" -> RenderType.cutout();
            case "cutout_mipped" -> RenderType.cutoutMipped();
            case "translucent" -> RenderType.translucent();
            default -> RenderType.solid();
        };
    }
}
