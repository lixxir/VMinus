package net.lixir.vminus.registry;

import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TaggedRegistryEntry<T, E> {
    E tags(List<TagKey<T>> tags);

    @NotNull List<TagKey<T>> getTags();

    E tag(TagKey<T> tag);
}
