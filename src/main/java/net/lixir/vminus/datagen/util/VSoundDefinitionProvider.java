package net.lixir.vminus.datagen.util;

import net.lixir.vminus.registry.SoundDefinitionInfo;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VSoundDefinitionProvider extends SoundDefinitionsProvider {
    private final String modId;

    public VSoundDefinitionProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    public void registerSounds() {
        for (SoundDefinitionInfo info : UnifiedRegistry.fromId(modId).getSoundDefinitionInfo()) {
            add(info.getSoundEvent(), generateSoundDefinition(info));
        }
    }

    protected SoundDefinition generateSoundDefinition(@NotNull SoundDefinitionInfo info) {
        String eventPath = ForgeRegistries.SOUND_EVENTS.getKey(info.getSoundEvent()).getPath();
        String subtitle = info.getSubtitle() != null ? (info.getSubtitle().equals("default") ? "subtitles." + eventPath : info.getSubtitle()) : null;
        String soundPath = info.getPath();
        SoundDefinition soundDefinition;
        List<String> paths = info.getPaths();
        if (paths != null && !paths.isEmpty()) {
            SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[paths.size()];
            for (int i = 0; i < paths.size(); i++) {
                sounds[i] = sound(new ResourceLocation(modId, paths.get(i)));
            }
            soundDefinition = definition().with(sounds);
        } else {
            int count = info.getCount();
            if (count == 1) {
                soundDefinition = definition().with(sound(new ResourceLocation(modId, soundPath)));
            } else {
                SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[count];
                for (int i = 0; i < count; i++) {
                    sounds[i] = sound(new ResourceLocation(modId, soundPath + (i + 1)));
                }
                soundDefinition = definition().with(sounds);
            }
        }
        if (subtitle == null)
            soundDefinition.subtitle(subtitle);
        return soundDefinition;
    }


    protected SoundDefinition multiVariantSound(String eventPath, String basePath, int variantCount) {
        return multiVariantSound(eventPath, "subtitles." + eventPath, basePath, variantCount);
    }

    protected SoundDefinition multiVariantSound(String eventPath, String subtitle, String basePath, int variantCount) {
        SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[variantCount];
        for (int i = 0; i < variantCount; i++) {
            sounds[i] = sound(new ResourceLocation(modId, basePath + (i + 1)));
        }

        return definition()
                .with(sounds)
                .subtitle(subtitle);
    }

    protected SoundDefinition singleSound(String eventPath, String subtitle, String soundPath) {
        return definition()
                .with(sound(new ResourceLocation(modId, soundPath)))
                .subtitle(subtitle);
    }

    protected SoundDefinition singleSound(String eventPath, String soundPath) {
        return definition()
                .with(sound(new ResourceLocation(modId, soundPath)))
                .subtitle("subtitles." + eventPath);
    }

    protected SoundDefinition singleSoundWithoutSubtitle(String eventPath, String soundPath) {
        return definition()
                .with(sound(new ResourceLocation(modId, soundPath)));
    }

}
