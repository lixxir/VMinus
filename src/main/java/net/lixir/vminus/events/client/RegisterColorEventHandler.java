package net.lixir.vminus.events.client;


import net.lixir.vminus.registry.TintType;
import net.lixir.vminus.registry.VRegistry;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.accessor.BlockEntryAccessor;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.accessor.ItemEntryAccessor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterColorEventHandler {
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (VRegistry vRegistry : VRegistry.getRegistries()) {
            for (Item item : vRegistry.getItems()) {
                ItemEntryAccessor accessor = (ItemEntryAccessor) item;
                ItemEntry itemEntry = accessor.vminus$getEntry();
                if (itemEntry == null)
                    continue;
                TintType tintType = itemEntry.getTint();
                if (tintType == TintType.UNSET || tintType == TintType.NONE)
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
        for (VRegistry vRegistry : VRegistry.getRegistries()) {
            for (Block block : vRegistry.getBlocks()) {
                BlockEntryAccessor accessor = (BlockEntryAccessor) block;
                BlockEntry blockEntry = accessor.vminus$getEntry();
                if (blockEntry == null)
                    continue;
                TintType tintType = blockEntry.getTintType();
                if (tintType == TintType.UNSET || tintType == TintType.NONE)
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