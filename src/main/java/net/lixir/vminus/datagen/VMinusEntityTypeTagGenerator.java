package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class VMinusEntityTypeTagGenerator extends EntityTypeTagsProvider {
    public VMinusEntityTypeTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, VMinus.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        var banned = tag(VMinusTags.Entities.BANNED);

        var zombies = tag(VMinusTags.Entities.ZOMBIES);
        zombies.add(EntityType.ZOMBIE);
        zombies.add(EntityType.HUSK);
        zombies.add(EntityType.DROWNED);
    }
}
