package net.lixir.vminus.events.client;


import net.lixir.vminus.VMinus;
import net.lixir.vminus.api.registry.VRegistry;
import net.lixir.vminus.api.tint.BlockTintFunction;
import net.lixir.vminus.api.tint.ItemTintFunction;
import net.lixir.vminus.api.tint.TintType;
import net.lixir.vminus.api.registry.definition.BlockDefinition;
import net.lixir.vminus.api.registry.definition.duck.BlockDefinitionDuck;
import net.lixir.vminus.api.registry.definition.ItemDefinition;
import net.lixir.vminus.api.registry.definition.duck.ItemDefinitionDuck;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterColorEventHandler {
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (VRegistry vRegistry : VRegistry.getRegistries()) {
            for (Item item : vRegistry.getItems()) {
                ItemDefinitionDuck accessor = (ItemDefinitionDuck) item;
                ItemDefinition itemEntry = accessor.vMinus$getDefinition();
                if (itemEntry == null)
                    continue;
                TintType tintType = itemEntry.getTintType();
                if (tintType.isEmpty())
                    continue;
                ItemTintFunction itemTintFunction = tintType.getItemTint();
                if (itemTintFunction != null)
                    event.register(itemTintFunction::apply, item);

            }
        }
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (VRegistry vRegistry : VRegistry.getRegistries()) {
            for (Block block : vRegistry.getBlocks()) {
                BlockDefinition blockDefinition = BlockDefinition.of(block);
                TintType tintType = blockDefinition.getTintType();
                if (tintType.isEmpty())
                    continue;
                BlockTintFunction blockTintFunction = tintType.getBlockTint();
                if (blockTintFunction != null) {
                    event.register(blockTintFunction::apply, block);
                    VMinus.LOGGER.info("Block Color registered for {}", block);
                }
            }
        }
    }
}