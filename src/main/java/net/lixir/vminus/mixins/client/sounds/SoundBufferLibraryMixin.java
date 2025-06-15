package net.lixir.vminus.mixins.client.sounds;

import com.mojang.blaze3d.audio.SoundBuffer;
import net.lixir.vminus.VMinus;
import net.lixir.vminus.audio.OggOpusAudioStream;
import net.minecraft.Util;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {
    @Shadow
    @Final
    private ResourceProvider resourceManager;

    @Shadow
    @Final
    private Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache;

    @Unique
    private final SoundBufferLibrary vminus$soundBufferLibrary = (SoundBufferLibrary) (Object) this;

    @Inject(method = "getStream", at = @At("RETURN"), cancellable = true)
    private void detour$injectOpusSupport(ResourceLocation location, boolean looping, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
        VMinus.LOGGER.info("[Opus Check] Stream requested: {}", location);
        VMinus.LOGGER.info("[Opus Check] Stream Ends with .opus? {}", location.getPath().endsWith(".opus"));

        if (location.getPath().endsWith(".opus")) {
            VMinus.LOGGER.info("Loading Opus stream: {}", location);
            ResourceProvider provider = ((SoundBufferLibraryAccessor) vminus$soundBufferLibrary).detour$getResourceProvider();

            cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
                try {
                    InputStream stream = provider.open(location);
                    if (looping) {
                        return new LoopingAudioStream(OggOpusAudioStream::new, stream);
                    } else {
                        return new OggOpusAudioStream(stream);
                    }
                } catch (IOException e) {
                    throw new CompletionException("Failed to load Opus stream: " + location, e);
                }
            }, Util.backgroundExecutor()));
        }
    }


    @Inject(method = "getCompleteBuffer", at = @At("RETURN"), cancellable = true)
    public void supportOpus(ResourceLocation location, CallbackInfoReturnable<CompletableFuture<SoundBuffer>> cir) {
        VMinus.LOGGER.info("[Opus Check] Buffer requested: {}", location);
        VMinus.LOGGER.info("[Opus Check] Buffer Ends with .opus? {}", location.getPath().endsWith(".opus"));

        if (location.getPath().endsWith(".opus")) {
            VMinus.LOGGER.info("Loading Opus Buffer: {}", location);
            if (this.cache.containsKey(location)) {
                this.cache.remove(location);
                VMinus.LOGGER.info("[Opus Cache] Already cached: {}", location);
            } else {
                VMinus.LOGGER.info("[Opus Cache] Not cached, creating new stream: {}", location);
            }
            cir.setReturnValue(this.cache.computeIfAbsent(location, loc ->
                    CompletableFuture.supplyAsync(() -> {

                        VMinus.LOGGER.info("[Opus Test] Inside supplyAsync");
                        try (
                                InputStream input = resourceManager.open(loc);
                                OggOpusAudioStream stream = new OggOpusAudioStream(input)
                        ) {
                            VMinus.LOGGER.info("Opened Opus stream successfully: {}", loc);
                            ByteBuffer buffer = stream.readAll();
                            VMinus.LOGGER.info("Read full Opus stream: {} bytes", buffer.limit());
                            return new SoundBuffer(buffer, stream.getFormat());
                        } catch (IOException e) {
                            VMinus.LOGGER.error("IO error while loading Opus stream {}: {}", loc, e.getMessage(), e);
                            throw new CompletionException(e);
                        } catch (Throwable t) {
                            VMinus.LOGGER.error("Unexpected error while loading Opus stream {}: {}", loc, t.getMessage(), t);
                            throw new CompletionException(t);
                        }
                    }, Util.backgroundExecutor())

            ));
        }
    }
}
