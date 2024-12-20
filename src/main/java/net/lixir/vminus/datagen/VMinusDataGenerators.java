package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = VMinusMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        VMinusBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new VMinusBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
    }
}