package net.lixir.vminus.audio;

import net.minecraft.resources.FileToIdConverter;
import org.spongepowered.asm.mixin.Unique;

public class OpusAudioResources {
    public static final FileToIdConverter SOUND_LISTER_OPUS = new FileToIdConverter("sounds", ".opus");
}
