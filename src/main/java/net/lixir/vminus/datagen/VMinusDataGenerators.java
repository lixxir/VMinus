package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VMinusMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        BlockTagsProvider blockTagsProvider = new VMinusBlockTagGenerator(generator, existingFileHelper);

        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new VMinusItemTagGenerator(generator, existingFileHelper, blockTagsProvider));

    }
}
