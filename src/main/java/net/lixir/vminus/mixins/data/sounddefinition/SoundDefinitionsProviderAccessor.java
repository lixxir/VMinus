package net.lixir.vminus.mixins.data.sounddefinition;

import net.minecraft.data.CachedOutput;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mixin(SoundDefinitionsProvider.class)
public interface SoundDefinitionsProviderAccessor {
    @Accessor("sounds")
    Map<String, SoundDefinition> getSoundList();

    @Invoker("save")
    CompletableFuture<?> invokeSave(final CachedOutput cache, final Path targetFile);
}
