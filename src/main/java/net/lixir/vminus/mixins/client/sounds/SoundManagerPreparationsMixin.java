package net.lixir.vminus.mixins.client.sounds;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.audio.OpusAudioResources;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(targets = "net.minecraft.client.sounds.SoundManager$Preparations")
public class SoundManagerPreparationsMixin {
    @Shadow
    private Map<ResourceLocation, Resource> soundCache;


    @Inject(method = "listResources", at = @At("TAIL"))
    private void detour$includeOpusFilesEarly(ResourceManager manager, CallbackInfo ci) {
        Map<ResourceLocation, Resource> opus = OpusAudioResources.SOUND_LISTER_OPUS.listMatchingResources(manager);

        soundCache.putAll(opus);
        if (!opus.isEmpty()) {
            net.lixir.vminus.VMinus.LOGGER.info("Loaded {} Opus sounds: {}", opus.size(), opus.keySet());
        }
    }
}
