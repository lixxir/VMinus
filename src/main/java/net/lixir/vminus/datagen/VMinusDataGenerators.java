package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = VMinusMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        BlockTagsProvider blockTagsProvider = new VMinusBlockTagGenerator(generator, existingFileHelper);
        ItemTagsProvider itemTagsProvider = new VMinusItemTagGenerator(generator, existingFileHelper, blockTagsProvider);

        if (event.includeServer()) {
            generator.addProvider(blockTagsProvider);
            generator.addProvider(itemTagsProvider);
        }

    }
}
