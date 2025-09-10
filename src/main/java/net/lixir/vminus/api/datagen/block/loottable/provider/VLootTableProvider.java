package net.lixir.vminus.api.datagen.block.loottable.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class VLootTableProvider {
    @Contract("_, _ -> new")
    public static @NotNull LootTableProvider create(PackOutput output, @NotNull Supplier<? extends VBlockLootProvider> vBlockSupplier) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(vBlockSupplier::get, LootContextParamSets.BLOCK)
        ));
    }
}
