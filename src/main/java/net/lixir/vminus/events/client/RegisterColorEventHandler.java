package net.lixir.vminus.events.client;

import net.lixir.vminus.registry.util.BlockSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterColorEventHandler {
    @SubscribeEvent
    public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
        for (BlockSet blockset : BlockSet.BLOCK_SETS) {
            if (blockset.hasLeaves() && blockset.areLeavesColored()) {
                Block leavesBlock = blockset.getLeavesBlock();
                event.register((stack, tintIndex) ->
                        tintIndex == 0 ? GrassColor.getDefaultColor() : -1, leavesBlock
                );
            }
        }

    }

    @SubscribeEvent
    public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
        for (BlockSet blockset : BlockSet.BLOCK_SETS) {
            if (blockset.hasLeaves() && blockset.areLeavesColored()) {
                Block leavesBlock = blockset.getLeavesBlock();
                event.getBlockColors().register(
                        (bs, world, pos, index) -> world != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(world, pos)
                                : FoliageColor.getDefaultColor(),
                        leavesBlock
                );
            }
        }
    }
}