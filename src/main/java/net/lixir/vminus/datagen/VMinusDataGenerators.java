package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.block.VMinusBlockTagProvider;
import net.lixir.vminus.datagen.item.VMinusItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = VMinus.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VMinusDataGenerators {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        CompletableFuture<TagsProvider.TagLookup<Block>> blockTagLookup = generator.addProvider(
                event.includeServer(),
                new VMinusBlockTagProvider(packOutput, lookupProvider, existingFileHelper)
        ).contentsGetter();
        generator.addProvider(
                event.includeServer(),
                new VMinusEntityTypeTagGenerator(packOutput, lookupProvider, existingFileHelper)
        );
        generator.addProvider(
                event.includeServer(),
                new VMinusVisionProvider(packOutput)
        );
        generator.addProvider(
                event.includeServer(),
                new VMinusItemTagProvider(packOutput, lookupProvider, blockTagLookup, existingFileHelper)
        );
        generator.addProvider(event.includeClient(), new VMinusLangProvider(packOutput, "en_us"));
    }
}