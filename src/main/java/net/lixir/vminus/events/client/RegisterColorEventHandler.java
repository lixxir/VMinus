package net.lixir.vminus.events.client;


import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.ItemEntryAccessor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.Item;
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
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (UnifiedRegistry unifiedRegistry : UnifiedRegistry.getRegistries()) {
            for (Item item : UnifiedRegistry.fromId(unifiedRegistry.getModId()).getItems()) {
                ItemEntryAccessor accessor = (ItemEntryAccessor) item;
                ItemEntry itemEntry = accessor.vminus$getEntry();
                if (itemEntry == null)
                    continue;
                TintType tintType = itemEntry.getTintType();
                if (tintType == null)
                    continue;
                switch (tintType) {
                    case FOLIAGE -> event.register((stack, tintIndex) -> tintIndex == 0 ? FoliageColor.getDefaultColor() : -1, item);
                    case GRASS ->  event.register((stack, tintIndex) -> tintIndex == 0 ? GrassColor.getDefaultColor() : -1, item);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (UnifiedRegistry unifiedRegistry : UnifiedRegistry.getRegistries()) {
            for (Block block : UnifiedRegistry.fromId(unifiedRegistry.getModId()).getBlocks()) {
                BlockEntryAccessor accessor = (BlockEntryAccessor) block;
                BlockEntry blockEntry = accessor.vminus$getEntry();
                if (blockEntry == null)
                    continue;
                TintType tintType = blockEntry.getTintType();
                if (tintType == null)
                    continue;
                switch (tintType) {
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