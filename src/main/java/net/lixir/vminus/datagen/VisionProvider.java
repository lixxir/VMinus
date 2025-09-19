package net.lixir.vminus.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lixir.vminus.vision.VisionProperty;
import net.lixir.vminus.vision.VisionType;

import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class VisionProvider implements DataProvider {
    private final PackOutput output;
    protected final String modId;
    private final Map<ResourceLocation, VisionData<?>> registeredVisions = new HashMap<>();

    public VisionProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    protected abstract void addVisions();

    private <T> void register(VisionData<T> visionData) {
        registeredVisions.put(visionData.getLocation(), visionData);
    }

    @SafeVarargs
    protected final VisionData.Builder<Item> itemVision(ResourceKey<Item>... resourceKeys) {
        return vision(VisionTypes.ITEM).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<Item> itemVision(TagKey<Item>... tags) {
        return vision(VisionTypes.ITEM).isFor(tags);
    }

    protected VisionData.Builder<Item> itemVision(Item... items) {
        return vision(VisionTypes.ITEM).isFor(items);
    }

    @SafeVarargs
    protected final VisionData.Builder<Block> blockVision(ResourceKey<Block>... resourceKeys) {
        return vision(VisionTypes.BLOCK).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<Block> blockVision(TagKey<Block>... tags) {
        return vision(VisionTypes.BLOCK).isFor(tags);
    }

    protected VisionData.Builder<Block> blockVision(Block... blocks) {
        return vision(VisionTypes.BLOCK).isFor(blocks);
    }

    @SafeVarargs
    protected final VisionData.Builder<EntityType<?>> entityVision(ResourceKey<EntityType<?>>... resourceKeys) {
        return vision(VisionTypes.ENTITY).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<EntityType<?>> entityVision(TagKey<EntityType<?>>... tags) {
        return vision(VisionTypes.ENTITY).isFor(tags);
    }

    protected VisionData.Builder<EntityType<?>> entityVision(EntityType<?>... entities) {
        return vision(VisionTypes.ENTITY).isFor(entities);
    }

    @SafeVarargs
    protected final VisionData.Builder<Enchantment> enchantmentVision(ResourceKey<Enchantment>... resourceKeys) {
        return vision(VisionTypes.ENCHANTMENT).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<Enchantment> enchantmentVision(TagKey<Enchantment>... tags) {
        return vision(VisionTypes.ENCHANTMENT).isFor(tags);
    }

    protected VisionData.Builder<Enchantment> enchantmentVision(Enchantment... enchantments) {
        return vision(VisionTypes.ENCHANTMENT).isFor(enchantments);
    }

    @SafeVarargs
    protected final VisionData.Builder<MobEffect> effectVision(ResourceKey<MobEffect>... resourceKeys) {
        return vision(VisionTypes.EFFECT).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<MobEffect> effectVision(TagKey<MobEffect>... tags) {
        return vision(VisionTypes.EFFECT).isFor(tags);
    }

    protected VisionData.Builder<MobEffect> effectVision(MobEffect... effects) {
        return vision(VisionTypes.EFFECT).isFor(effects);
    }

    @SafeVarargs
    protected final VisionData.Builder<CreativeModeTab> tabVision(ResourceKey<CreativeModeTab>... resourceKeys) {
        return vision(VisionTypes.TAB).isFor(resourceKeys);
    }

    @SafeVarargs
    protected final VisionData.Builder<CreativeModeTab> tabVision(TagKey<CreativeModeTab>... tags) {
        return vision(VisionTypes.TAB).isFor(tags);
    }

    protected VisionData.Builder<CreativeModeTab> tabVision(CreativeModeTab... tabs) {
        return vision(VisionTypes.TAB).isFor(tabs);
    }

    protected <T> VisionData.Builder<T> vision(VisionType<T> visionType) {
        return VisionData.Builder.of(visionType);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        addVisions();
        return CompletableFuture.allOf(
                registeredVisions.entrySet().stream()
                        .map(entry -> saveVision(cache, entry.getKey(), entry.getValue()))
                        .toArray(CompletableFuture[]::new)
        );
    }

    private @NotNull CompletableFuture<?> saveVision(CachedOutput cache, ResourceLocation location, @NotNull VisionData<?> builder) {
        String locationNamespace = location.getNamespace();
        String locationPath = location.getPath();
        Path path = output.getOutputFolder().resolve("data/" + locationNamespace + "/" + builder.getType().getDirectory() +"/" + locationPath + ".json");

        JsonObject json = builder.toJson();
        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public @NotNull String getName() {
        return "Vision Provider [" + modId + "]";
    }


    public static class VisionData<T> {
        public VisionType<T> getType() {
            return type;
        }

        private final VisionType<T> type;
        private final List<String> appliesTo;
        private final Map<VisionProperty<?>, JsonArray> properties;
        private final ResourceLocation location;

        @SuppressWarnings("unchecked")
        private VisionData(Builder builder, ResourceLocation location) {
            this.type = (VisionType<T>) builder.type;
            this.appliesTo = builder.appliesTo;
            this.properties = builder.properties;
            this.location = location;
        }

        public ResourceLocation getLocation() {
            return location;
        }

        public static class Builder<T> {
            private final VisionType<T> type;
            private final List<String> appliesTo = new ArrayList<>();
            private final Map<VisionProperty<?>, JsonArray> properties = new HashMap<>();

            private Builder(VisionType<T> type) {
                this.type = type;
            }

            @Contract("_ -> new")
            public static <T> @NotNull Builder<T> of(VisionType<T> type){
                return new Builder<>(type);
            }

            @Contract("_, _ -> this")
            @SafeVarargs
            public final <V> Builder<T> with(VisionProperty<V> visionProperty, V @NotNull ... values) {
                JsonArray array = properties.computeIfAbsent(visionProperty, k -> new JsonArray());
                for (V value : values)
                    array.add(visionProperty.getCodec().encode(value));
                return this;
            }

            @SafeVarargs
            public final Builder<T> isFor(TagKey<T> @NotNull ... applicants) {
                for (TagKey<T> applicant : applicants)
                    this.appliesTo.add("#" + applicant.location());
                return this;
            }

            @SafeVarargs
            public final Builder<T> isFor(ResourceKey<T> @NotNull ... applicants) {
                for (ResourceKey<T> applicant : applicants)
                    this.appliesTo.add(applicant.location().toString());
                return this;
            }

            @Contract("_ -> this")
            @SafeVarargs
            @SuppressWarnings({"unchecked", "rawtypes"})
            public final Builder<T> isFor(T @NotNull ... applicants) {
                for (T applicant : applicants) {
                    Registry raw = this.type.getRegistry();
                    ResourceLocation id = raw.getKey(applicant);
                    if (id == null) {
                        throw new IllegalArgumentException("No registry key found for " + applicant);
                    }
                    this.appliesTo.add(id.toString());
                }
                return this;
            }

            public Builder<T> isFor(String... appliesTo) {
                this.appliesTo.addAll(Arrays.asList(appliesTo));
                return this;
            }

            public VisionData<?> save(VisionProvider visionProvider, ResourceLocation location) {
                if (appliesTo.isEmpty())
                    throw new IllegalArgumentException("Vision must have at least 1 applicant defined.");
                if (properties.isEmpty())
                    throw new IllegalArgumentException("Vision must have at least 1 property defined.");
                VisionData<?> visionData = new VisionData<>(this, location);
                visionProvider.register(visionData);
                return visionData;
            }
        }

        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();

            JsonArray appliesArray = new JsonArray();
            for (String applicant : appliesTo) {
                appliesArray.add(applicant);
            }
            jsonObject.add(type.getMultiList(), appliesArray);

            for (Map.Entry<VisionProperty<?>, JsonArray> entry : properties.entrySet()) {
                jsonObject.add(entry.getKey().getId(), entry.getValue());
            }

            return jsonObject;
        }
    }
}
