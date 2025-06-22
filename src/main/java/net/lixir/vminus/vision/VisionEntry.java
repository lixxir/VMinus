package net.lixir.vminus.vision;

import net.lixir.vminus.vision.values.VisionProperty;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class VisionEntry<T> {
    private final List<String> entries = new ArrayList<>();
    private final HashMap<String, List<? extends VisionProperty<?>>> values = new HashMap<>();

    public boolean isEmpty() {
        return this.entries.isEmpty() || this.values.isEmpty();
    }

    public static boolean visionApplies(@Nullable Object object, String id, List<String> applicantList, @Nullable ICondition.IContext context) {
        boolean invalidMatch = false;
        boolean validMatchFound = false;

        String[] idParts = id.split(":", 2);
        String idPath = idParts.length > 1 ? idParts[1] : idParts[0];

        for (String rawMatchKey : applicantList) {
            boolean inverted = rawMatchKey.startsWith("!");
            String matchKey = inverted ? rawMatchKey.substring(1) : rawMatchKey;

            boolean isTag = matchKey.startsWith("#");
            if (isTag) matchKey = matchKey.substring(1);

            String[] matchParts = matchKey.split(":", 2);
            String matchNamespace = matchParts.length > 1 ? matchParts[0] : "";
            String matchPath = matchParts.length > 1 ? matchParts[1] : matchParts[0];

            boolean found =
                    matchKey.equals("all") ||
                            wildcardMatches(id, matchKey) ||
                            (matchNamespace.isEmpty() && (idPath.equals(matchPath) || wildcardMatches(idPath, matchPath))) ||
                            (!isTag && (id.equals(matchKey) || wildcardMatches(id, matchKey))) ||
                            (isTag && (
                                    (object instanceof Item item && isItemTagged(item, new ResourceLocation(matchKey), context)) ||
                                            (object instanceof Block block && isBlockTagged(block, new ResourceLocation(matchKey), context)) ||
                                            (object instanceof EntityType<?> entityType && isEntityTagged(entityType, new ResourceLocation(matchKey), context))
                            ));

            if (inverted) found = !found;

            if (!inverted && found) {
                validMatchFound = true;
            } else if (inverted && !found) {
                invalidMatch = true;
                break;
            }
        }

        return !invalidMatch && validMatchFound;
    }

    private static boolean isItemTagged(Item item, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        return isTagged(item, matchKey, context, ForgeRegistries.ITEMS,
                key -> TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), key));
    }

    private static boolean isBlockTagged(Block block, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        return isTagged(block, matchKey, context, ForgeRegistries.BLOCKS,
                key -> TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), key));
    }

    private static boolean isEntityTagged(EntityType<?> entityType, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        return isTagged(entityType, matchKey, context, ForgeRegistries.ENTITY_TYPES,
                key -> TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), key));
    }

    private static <T> boolean isTagged(T object, ResourceLocation matchKey, ICondition.IContext context, IForgeRegistry<T> registry, Function<ResourceLocation, TagKey<T>> tagKeyFactory) {
        if (context == null)
            return false;

        TagKey<T> tagKey = tagKeyFactory.apply(matchKey);
        Collection<Holder<T>> tags = context.getTag(tagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation objectKey = registry.getKey(object);

        if (objectKey == null)
            return false;

        return tags.stream().anyMatch(holder -> Objects.equals(registry.getKey(holder.value()), objectKey));
    }

    private static boolean wildcardMatches(String value, String pattern) {
        if (pattern.equals("*"))
            return true;

        if (pattern.startsWith("*")) {
            if (pattern.endsWith("*")) {
                return value.contains(pattern.substring(1, pattern.length() - 1));
            }
            return value.endsWith(pattern.substring(1));
        } else if (pattern.endsWith("*")) {
            return value.startsWith(pattern.substring(0, pattern.length() - 1));
        }

        return value.equals(pattern);
    }

    public HashMap<String, List<? extends VisionProperty<?>>> getValues() {
        return values;
    }

    public List<String> getEntries() {
        return entries;
    }

    public void addValues(String key, List<? extends VisionProperty<?>> values) {
        this.values.merge(key, new ArrayList<>(values), (existingList, newList) -> {
            ArrayList<VisionProperty<?>> combined = new ArrayList<>(existingList);
            combined.addAll(newList);
            return combined;
        });
    }

    public void addEntries(ArrayList<String> newEntries) {
        this.entries.addAll(newEntries);
    }

    public void merge(VisionEntry<T> visionEntry) {
        this.entries.addAll(visionEntry.getEntries());


        for (var entry : visionEntry.values.entrySet()) {
            String key = entry.getKey();
            List<? extends VisionProperty<?>> incomingValues = entry.getValue();

            this.values.merge(key, new ArrayList<>(incomingValues), (existingList, newList) -> {
                ArrayList<VisionProperty<?>> combined = new ArrayList<>(existingList);
                combined.addAll(newList);
                return combined;
            });
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VisionEntry");

        sb.append("  entries = ").append(entries).append(",\n");

        sb.append("  values = {\n");
        for (var entry : values.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(" = ");
            sb.append(entry.getValue()).append(",\n");
        }
        sb.append("  }\n");

        sb.append("}");
        return sb.toString();
    }




}

