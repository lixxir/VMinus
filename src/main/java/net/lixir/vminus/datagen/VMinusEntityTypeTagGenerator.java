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
        tag(VMinusTags.Entities.BANNED);
        tag(VMinusTags.Entities.CREATIVE_ONLY)
                .add(EntityType.MARKER)
                .add(EntityType.ILLUSIONER)
                .add(EntityType.TEXT_DISPLAY)
                .add(EntityType.GIANT)
                .add(EntityType.INTERACTION)
                .add(EntityType.BLOCK_DISPLAY)
                .add(EntityType.SPAWNER_MINECART)
                .add(EntityType.COMMAND_BLOCK_MINECART);
        tag(VMinusTags.Entities.ZOMBIES)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.DROWNED);
    }
}
