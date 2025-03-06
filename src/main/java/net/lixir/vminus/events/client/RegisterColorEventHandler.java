package net.lixir.vminus.events.client;

import net.lixir.vminus.util.setup.SetupRegistries;
import net.lixir.vminus.util.setup.SetupTint;
import net.lixir.vminus.util.setup.block.BlockSetup;
import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterColorEventHandler {
    @SubscribeEvent
    public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
        for (List<BlockSetup> blockSetupList : SetupRegistries.BLOCKS.getValues().values()) {
            for (BlockSetup blockSetup : blockSetupList) {
                BlockItemRegistryPair blockItemPair = blockSetup.getBlockItemPair();
                Item item = blockItemPair.item();
                SetupTint setupTint = blockSetup.getDatagenTint();
                if (item != null && setupTint != null && !setupTint.equals(SetupTint.NONE)) {
                    switch (setupTint) {
                        case FOLIAGE -> event.register((stack, tintIndex) ->
                                tintIndex == 0 ? FoliageColor.getDefaultColor() : -1, item
                        );
                        case GRASS -> event.register((stack, tintIndex) ->
                                tintIndex == 0 ? GrassColor.getDefaultColor() : -1, item
                        );
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
        for (List<BlockSetup> blockSetupList : SetupRegistries.BLOCKS.getValues().values()) {
            for (BlockSetup blockSetup : blockSetupList) {
                BlockItemRegistryPair blockItemPair = blockSetup.getBlockItemPair();
                Block block = blockItemPair.block();
                SetupTint setupTint = blockSetup.getDatagenTint();
                if (block != null && setupTint != null && !setupTint.equals(SetupTint.NONE)) {
                    switch (setupTint) {
                        case FOLIAGE -> event.getBlockColors().register(
                                (bs, world, pos, index) -> world != null && pos != null
                                        ? BiomeColors.getAverageFoliageColor(world, pos)
                                        : FoliageColor.getDefaultColor(),
                                block
                        );
                        case GRASS -> event.getBlockColors().register(
                                (bs, world, pos, index) -> world != null && pos != null
                                        ? BiomeColors.getAverageGrassColor(world, pos)
                                        : GrassColor.getDefaultColor(),
                                block
                        );
                    }
                }
            }
        }
    }
}