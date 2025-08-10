package net.lixir.vminus.datagen.util.tag;

import net.lixir.vminus.registry.VRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class VFluidTagsProvider extends FluidTagsProvider {
    protected final String modId;

    public VFluidTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookup, modId, existingFileHelper);
        this.modId = modId;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (Fluid fluid : VRegistry.fromId(modId).getFluids()) {
            ResourceLocation location = BuiltInRegistries.FLUID.getKey(fluid);
            String name = location.getPath();
            if (name.startsWith("flowing_"))
                continue;

            var tagKey = FluidTags.create(new ResourceLocation(modId, name));
            tag(tagKey).add(fluid);

            ResourceLocation flowingRl = new ResourceLocation(location.getNamespace(), "flowing_" + name);
            Fluid flowingFluid = BuiltInRegistries.FLUID.get(flowingRl);
            if (!flowingFluid.isSame(Fluids.EMPTY)) {
                this.tag(tagKey).add(flowingFluid);
            }
        }
    }
}
