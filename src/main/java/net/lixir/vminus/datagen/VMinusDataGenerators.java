package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.datagen.blockset.BlockSetStateProvider;
import net.lixir.vminus.registry.util.BlockSet;
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


        generator.addProvider(event.includeServer(), new VMinusRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), VMinusLootTableProvider.create(packOutput));

        VMinusBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new VMinusBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));

        for (String modId : BlockSet.usingMods) {
            generator.addProvider(event.includeClient(), new BlockSetStateProvider(packOutput, existingFileHelper, modId));
        }
    }
}