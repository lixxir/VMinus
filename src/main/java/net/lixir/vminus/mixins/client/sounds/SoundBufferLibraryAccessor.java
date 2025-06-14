package net.lixir.vminus.mixins.client.sounds;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mixin(SoundBufferLibrary.class)
public interface SoundBufferLibraryAccessor {
    @Accessor("resourceManager")
    ResourceProvider detour$getResourceProvider();

    @Accessor("cache")
    Map<ResourceLocation, CompletableFuture<SoundBuffer>> getCache();

}
