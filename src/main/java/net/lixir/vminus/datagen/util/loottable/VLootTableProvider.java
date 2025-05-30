package net.lixir.vminus.datagen.util.loottable;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class VLootTableProvider {
    public static LootTableProvider create(PackOutput output, Supplier<? extends VBlockLootTables> vBlockSupplier) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(vBlockSupplier::get, LootContextParamSets.BLOCK)
        ));
    }
}
