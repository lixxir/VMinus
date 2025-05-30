package net.lixir.vminus.registry;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoundDefinitionInfo {
    private final List<String> paths;
    private SoundEvent soundEvent = null;
    private final String path;
    private final String subtitle;
    private final int count;

    private SoundDefinitionInfo(@Nullable String subtitle, @Nullable String path, int count, @Nullable List<String> paths) {
        this.path = path;
        this.subtitle = subtitle;
        this.count = count;
        this.paths = paths;
    }

    public static @NotNull SoundDefinitionInfo of(List<String> paths) {
        return of("default", null, 1, paths);
    }

    public static @NotNull SoundDefinitionInfo of(@Nullable String subtitle, List<String> paths) {
        return of(subtitle, null, 1, paths);
    }

    public static @NotNull SoundDefinitionInfo of(String path) {
        return of("default", path, 1, null);
    }

    public static @NotNull SoundDefinitionInfo of(@Nullable String subtitle, String path) {
        return of(subtitle, path, 1, null);
    }

    public static @NotNull SoundDefinitionInfo of(@Nullable String subtitle, String path, int count) {
        return of(subtitle, path, count, null);
    }

    public static @NotNull SoundDefinitionInfo of(String path, int count) {
        return of("default", path, count, null);
    }

    public static @NotNull SoundDefinitionInfo of(@Nullable String subtitle, @Nullable String path, int count, @Nullable List<String> paths) {
        if (count <= 0)
            throw new IllegalArgumentException("Variant count must be at least 1 for sound event: " + path);
        return new SoundDefinitionInfo(subtitle, path, count, paths);
    }

    public @Nullable String getSubtitle() {
        return subtitle;
    }

    public String getPath() {
        return path;
    }

    public int getCount() {
        return count;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public SoundDefinitionInfo setSoundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    public List<String> getPaths() {
        return paths;
    }
}