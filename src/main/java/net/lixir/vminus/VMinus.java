package net.lixir.vminus;

import net.lixir.vminus.attribute.VMinusAttributes;
import net.lixir.vminus.block.VMinusBlocks;
import net.lixir.vminus.item.VMinusItems;
import net.lixir.vminus.item.trait.ItemTraits;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.lixir.vminus.registry.VMinusRegistryEntryDefaults;
import net.lixir.vminus.registry.VMinusSounds;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod("vminus")
public class VMinus {
    public static final Logger LOGGER = LogManager.getLogger(VMinus.class);
    public static final String ID = "vminus";
    public static final UnifiedRegistry REGISTRY = UnifiedRegistry.create(ID, reg -> {
        VisionTypes.init();
        VMinusRegistryEntryDefaults.init();
        VMinusBlocks.init();
        VMinusItems.init();
        VMinusSounds.init();
        VMinusAttributes.init();
    });

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public VMinus(@NotNull FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = context.getModEventBus();
        ItemTraits.TRAITS.register(bus);
        context.registerConfig(ModConfig.Type.COMMON, VMinusConfig.COMMON_CONFIG);
    }

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.@NotNull ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
}
