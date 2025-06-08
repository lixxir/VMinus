package net.lixir.vminus.registry.entry;

import net.lixir.vminus.registry.TaggedRegistryEntry;
import net.lixir.vminus.registry.TintType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityEntry extends RegistryEntry<EntityEntry, EntityType<?>> implements TaggedRegistryEntry<EntityEntry, EntityType<?>> {
    protected final List<TagKey<EntityType<?>>> tags = new ArrayList<>();

    private EntityEntry() {}

    @Contract(value = " -> new", pure = true)
    public static @NotNull EntityEntry of() {
        return new EntityEntry();
    }

    @Override
    public EntityEntry tags(List<TagKey<EntityType<?>>> tags) {
        this.tags.addAll(tags);
        return this;
    }

    @Override
    public @NotNull List<TagKey<EntityType<?>>> getTags() {
        return tags;
    }

    @Override
    public EntityEntry tag(TagKey<EntityType<?>> tag) {
        this.tags.add(tag);
        return this;
    }

    @Override
    public EntityEntry lang(String langValue) {
        this.langValue = langValue;
        return this;
    }

    @Override
    void merge(EntityEntry self, EntityEntry other) {

    }
}
