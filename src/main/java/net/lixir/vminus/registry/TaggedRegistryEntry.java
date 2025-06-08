package net.lixir.vminus.registry;

import net.lixir.vminus.registry.entry.RegistryEntry;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TaggedRegistryEntry<E extends RegistryEntry<E,T>, T> {
    E tags(List<TagKey<T>> tags);

    @NotNull List<TagKey<T>> getTags();

    E tag(TagKey<T> tag);
}
