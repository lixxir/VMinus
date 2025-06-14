package net.lixir.vminus.datagen.util;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.mixins.SoundDefinitionAccessor;
import net.lixir.vminus.mixins.SoundDefinitionSoundAccessor;
import net.lixir.vminus.mixins.SoundDefinitionsProviderAccessor;
import net.lixir.vminus.registry.SoundDefinitionInfo;
import net.lixir.vminus.registry.UnifiedRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VSoundDefinitionProvider extends SoundDefinitionsProvider {
    protected final PackOutput output;
    protected final String modId;
    protected final ExistingFileHelper helper;

    public VSoundDefinitionProvider(PackOutput output, ExistingFileHelper helper, String modId) {
        super(output, modId, helper);
        this.output = output;
        this.modId = modId;

        this.helper = helper;
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
        if (subtitle != null)
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


    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var accessor =  ((SoundDefinitionsProviderAccessor) this);
        var sounds = accessor.getSoundList();
        sounds.clear();
        this.registerSounds();

        List<String> notValid = sounds.entrySet().stream()
                .filter(it -> !this.customValidate(it.getKey(), it.getValue()))
                .map(Map.Entry::getKey)
                .map(it -> this.modId + ":" + it)
                .toList();

        if (!notValid.isEmpty()) {
            throw new IllegalStateException("Found invalid sound events: " + notValid);
        }

        if (!sounds.isEmpty()) {
            return accessor.invokeSave(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modId).resolve("sounds.json"));
        }
        return CompletableFuture.allOf();
    }

    private boolean customValidate(final String name, final SoundDefinition def) {
        var accessor =  ((SoundDefinitionAccessor) (Object) def);
        return accessor.getSoundList().stream().allMatch(it -> {
            var soundAccessor =  ((SoundDefinitionSoundAccessor) (Object) it);
            return customValidateSound(name, soundAccessor.getName());
        });
    }

    private boolean customValidateSound(final String soundName, final ResourceLocation name) {
        boolean oggExists = this.helper.exists(name, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        boolean opusExists = this.helper.exists(name, PackType.CLIENT_RESOURCES, ".opus", "sounds");
        boolean valid = oggExists || opusExists;

        if (!valid) {
            String oggPath = name.getNamespace() + ":sounds/" + name.getPath() + ".ogg";
            String opusPath = name.getNamespace() + ":sounds/" + name.getPath() + ".opus";
            VMinus.LOGGER.warn("Unable to find corresponding OGG or OPUS file '{}' or '{}' for sound event '{}'", oggPath, opusPath, soundName);
        }
        return valid;
    }

}
