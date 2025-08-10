package net.lixir.vminus.registry.entry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EntityEntry extends RegistryEntry<EntityEntry, EntityType<?>> {
    protected final Set<TagKey<EntityType<?>>> tags = new HashSet<>();

    private EntityEntry() {}

    @Contract(value = " -> new", pure = true)
    public static @NotNull EntityEntry of() {
        return new EntityEntry();
    }

    @SafeVarargs
    public final EntityEntry tag(TagKey<EntityType<?>>... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public @NotNull Set<TagKey<EntityType<?>>> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public EntityEntry lang(String lang) {
        this.lang = lang;
        return this;
    }

    @Override
    public @NotNull EntityEntry merge(EntityEntry other) {
        return this;
    }
}
