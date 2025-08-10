package net.lixir.vminus.events.client;

import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
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
                BlockEntryAccessor accessor = (BlockEntryAccessor) block;
                BlockEntry blockEntry = accessor.vminus$getEntry();
                if (blockEntry == null)
                    continue;
                RenderType renderType = getRenderType(blockEntry);
                ItemBlockRenderTypes.setRenderLayer(block, renderType);
            }
        }
    }

    private static @NotNull RenderType getRenderType(@NotNull BlockEntry blockEntry) {
        String rawRenderType = blockEntry.getRenderType().toLowerCase();
        return switch (rawRenderType) {
            case "cutout" -> RenderType.cutout();
            case "cutout_mipped" -> RenderType.cutoutMipped();
            case "translucent" -> RenderType.translucent();
            default -> RenderType.solid();
        };
    }
}
