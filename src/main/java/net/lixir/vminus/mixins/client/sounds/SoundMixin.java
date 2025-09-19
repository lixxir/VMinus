package net.lixir.vminus.mixins.client.sounds;

import net.lixir.vminus.api.audio.opus.OpusAudioResources;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sound.class)
public class SoundMixin {
    @Final
    @Shadow
    private ResourceLocation location;

    @Inject(method = "getPath", at = @At("RETURN"), cancellable = true)
    public void detour$fixOpusPath(CallbackInfoReturnable<ResourceLocation> cir) {
        if (location.getPath().endsWith(".opus")) {
            String path = location.getPath();
            path = path.substring(0, path.indexOf(".opus"));
            String namespace = location.getNamespace();
            ResourceLocation opusLocation = new ResourceLocation(namespace, path);
            cir.setReturnValue(OpusAudioResources.SOUND_LISTER_OPUS.idToFile(opusLocation));
        }
    }
}
