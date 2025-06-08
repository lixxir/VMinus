package net.lixir.vminus.mixins;

import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.lixir.vminus.audio.OpusAudioStream;
import net.minecraft.Util;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.concentus.OpusException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {

    @Shadow @Final
    private ResourceProvider resourceManager;

    @Shadow @Final
    private Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache;

    @Inject(method = "getCompleteBuffer", at = @At("HEAD"), cancellable = true)
    private void injectGetCompleteBuffer(ResourceLocation location, CallbackInfoReturnable<CompletableFuture<SoundBuffer>> cir) {
        CompletableFuture<SoundBuffer> future = this.cache.computeIfAbsent(location, id ->
                CompletableFuture.supplyAsync(() -> {
                    try (InputStream stream = resourceManager.open(id)) {
                        if (id.getPath().endsWith(".opus")) {
                            try (OpusAudioStream opus = new OpusAudioStream(stream)) {
                                ByteBuffer buffer = opus.readAll();
                                AudioFormat format = opus.getFormat();
                                return new SoundBuffer(buffer, format);
                            }
                        } else {
                            try (OggAudioStream ogg = new OggAudioStream(stream)) {
                                ByteBuffer buffer = ogg.readAll();
                                return new SoundBuffer(buffer, ogg.getFormat());
                            }
                        }
                    } catch (IOException | OpusException e) {
                        throw new CompletionException(e);
                    }
                }, Util.backgroundExecutor())
        );
        cir.setReturnValue(future);
    }
}
