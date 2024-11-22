package net.lixir.vminus.visions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class VisionHandler {
    public static final int ITEM_TYPE = 0;
    public static final int BLOCK_TYPE = 1;
    public static final int ENTITY_TYPE = 2;
    public static final int EFFECT_TYPE = 3;
    public static final int ENCHANTMENT_TYPE = 4;
    // json caches & keys for optimization
    private static final ConcurrentHashMap<String, Integer> ITEM_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> ITEM_VISION_CACHE = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, Integer> BLOCK_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> BLOCK_VISION_CACHE = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, Integer> ENTITY_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> ENTITY_VISION_CACHE = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, Integer> EFFECT_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> EFFECT_VISION_CACHE = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, Integer> ENCHANTMENT_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> ENCHANTMENT_VISION_CACHE = new CopyOnWriteArrayList<>();
    // tag & resource location caches
    private static final ConcurrentHashMap<String, ResourceLocation> RESOURCE_LOCATION_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<EntityType<?>>> ENTITY_TAG_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<Block>> BLOCK_TAG_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<Item>> ITEM_TAG_CACHE = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Integer> getItemVisionKey() {
        return ITEM_VISION_KEY;
    }
    public static CopyOnWriteArrayList<JsonObject> getItemVisionCache() {
        return ITEM_VISION_CACHE;
    }
    public static ConcurrentHashMap<String, Integer> getEntityVisionKey() {
        return ENTITY_VISION_KEY;
    }
    public static CopyOnWriteArrayList<JsonObject> getEntityVisionCache() {
        return ENTITY_VISION_CACHE;
    }

    public static JsonObject getVisionData(ItemStack itemstack) {
        return getVisionData(itemstack, false, null, null, null, null);
    }

    public static JsonObject getVisionData(MobEffect effect) {
        return getVisionData(null, false, null, null, effect, null);
    }

    public static JsonObject getVisionData(Enchantment enchantment) {
        return getVisionData(null, false, null, null, null, enchantment);
    }

    public static JsonObject getVisionData(Enchantment enchantment, boolean debug) {
        return getVisionData(null, debug, null, null, null, enchantment);
    }

    public static JsonObject getVisionData(ItemStack itemstack, Boolean debug) {
        return getVisionData(itemstack, debug, null, null, null, null);
    }

    public static JsonObject getVisionData(Block block) {
        return getVisionData(null, false, block, null, null, null);
    }

    public static JsonObject getVisionData(Block block, Boolean debug) {
        return getVisionData(null, debug, block, null, null, null);
    }

    public static JsonObject getVisionData(Entity entity, Boolean debug) {
        return getVisionData(null, debug, null, entity, null, null);
    }

    public static JsonObject getVisionData(Entity entity) {
        return getVisionData(null, false, null, entity, null, null);
    }

    public static void loadVisions() {
        if (VminusModVariables.main_item_vision != null) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                ItemStack itemStack = new ItemStack(item);
                VisionHandler.getVisionData(itemStack);
            }
        }
        if (VminusModVariables.main_block_vision != null) {
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                VisionHandler.getVisionData(block);
            }
        }
        if (VminusModVariables.main_effect_vision != null) {
            for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                VisionHandler.getVisionData(effect);
            }
        }
        if (VminusModVariables.main_enchantment_vision != null) {
            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                VisionHandler.getVisionData(enchantment);
            }
        }
        //Entities are not preloaded due to errors.
    }

    public static void clearCaches() {
        ITEM_VISION_KEY.clear();
        ITEM_VISION_CACHE.clear();

        BLOCK_VISION_KEY.clear();
        BLOCK_VISION_CACHE.clear();

        ENTITY_VISION_KEY.clear();
        ENTITY_VISION_CACHE.clear();

        EFFECT_VISION_KEY.clear();
        EFFECT_VISION_CACHE.clear();

        ENCHANTMENT_VISION_KEY.clear();
        ENCHANTMENT_VISION_CACHE.clear();
    }


    private static void cacheVision(Map<String, Integer> visionKey, CopyOnWriteArrayList<JsonObject> visionCache,
                                    JsonObject mergedData, String id) {
        if (mergedData != null && !mergedData.entrySet().isEmpty()) {
            int index = visionCache.indexOf(mergedData);
            if (index == -1) {
                visionCache.add(mergedData);
                index = visionCache.size() - 1;
            }
            if (!visionKey.containsKey(id)) {
                visionKey.put(id, index);
            }
        }
    }

    private static JsonObject scanVisionKey(JsonObject mainVision, String key, String id, JsonObject mergedData, @Nullable ItemStack itemstack, @Nullable Block block, @Nullable Entity entity) {
        final String originalKey = key;
        //key = key.replaceAll(" ", "");
        String[] parts = key.split(",");
        boolean matchExplicitlyAllowed = false;
        boolean requiredFailed = false;

        for (String matchKey : parts) {
            boolean found = false;
            boolean inverted = false;
            boolean required = false;
            if (matchKey.startsWith(" "))
                matchKey = matchKey.substring(1);

            if (matchKey.startsWith("&")) {
                required = true;
                matchKey = matchKey.substring(1);
            }

            if (matchKey.startsWith("!")) {
                inverted = true;
                matchKey = matchKey.substring(1);
            }

            if (matchKey.equals(id)) {
                found = true;
            }

            if (matchKey.equals("global")) {
                found = true;
            }

            if (itemstack != null && matchKey.startsWith("#")) {
                if (isItemTagged(itemstack, matchKey)) {
                    found = true;
                }
            }

            if (block != null && matchKey.startsWith("#")) {
                if (isBlockTagged(block, matchKey)) {
                    found = true;
                }
            }

            if (entity != null && matchKey.startsWith("#")) {
                if (isEntityTagged(entity, matchKey)) {
                    found = true;
                }
            }

            if (inverted) {
                found = !found;
            }

            if (required && !found) {
                requiredFailed = true;
                break;
            }


            if (found && !inverted) {
                matchExplicitlyAllowed = true;
            }
        }


        if (requiredFailed || !matchExplicitlyAllowed) {
            return mergedData;
        }

        JsonObject matchedData = mainVision.getAsJsonObject(originalKey);
        if (matchedData != null) {
            mergedData = mergeJsonObjects(mergedData, matchedData);
        }

        return mergedData;
    }

    public static JsonObject getVisionData(@Nullable ItemStack itemstack, @Nullable Boolean debug, @Nullable Block block, @Nullable Entity entity, @Nullable MobEffect effect, @Nullable Enchantment enchantment) {
        JsonObject mainVision;
        String id;
        byte type = -1;
        if (debug)
            VMinusMod.LOGGER.info("______________DEBUGGING______________");
        // Determines the type of vision and gets the id of the feature, or if it already exists in the cache, then use that.
        if (itemstack != null)
            type = ITEM_TYPE;
        if (block != null)
            type = BLOCK_TYPE;
        if (entity != null)
            type = ENTITY_TYPE;
        if (effect != null)
            type = EFFECT_TYPE;
        if (enchantment != null)
            type = ENCHANTMENT_TYPE;
        if (debug)
            VMinusMod.LOGGER.info("Type: " + type);
        if (type == -1)
            return null;
        switch (type) {
            case ITEM_TYPE:
                // items
                mainVision = VminusModVariables.main_item_vision;
                if (itemstack.hasTag() && itemstack.getOrCreateTag().contains("vision")) {
                    id = itemstack.getOrCreateTag().getString("vision");
                } else {
                    id = ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
                }
                if (ITEM_VISION_KEY.containsKey(id))
                    return ITEM_VISION_CACHE.get(ITEM_VISION_KEY.get(id));
                break;
            case BLOCK_TYPE:
                // blocks
                mainVision = VminusModVariables.main_block_vision;
                id = ForgeRegistries.BLOCKS.getKey(block).toString();

                if (BLOCK_VISION_KEY.containsKey(id))
                    return BLOCK_VISION_CACHE.get(BLOCK_VISION_KEY.get(id));
                break;
            case ENTITY_TYPE:
                // entities
                mainVision = VminusModVariables.main_entity_vision;
                id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();

                if (ENTITY_VISION_KEY.containsKey(id))
                    return ENTITY_VISION_CACHE.get(ENTITY_VISION_KEY.get(id));
                break;
            case EFFECT_TYPE:
                // effects
                mainVision = VminusModVariables.main_effect_vision;
                id = ForgeRegistries.MOB_EFFECTS.getKey(effect).toString();

                if (EFFECT_VISION_KEY.containsKey(id))
                    return EFFECT_VISION_CACHE.get(EFFECT_VISION_KEY.get(id));
                break;
            case ENCHANTMENT_TYPE:
                // enchantments
                mainVision = VminusModVariables.main_enchantment_vision;
                id = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();

                if (ENCHANTMENT_VISION_KEY.containsKey(id))
                    return ENCHANTMENT_VISION_CACHE.get(ENCHANTMENT_VISION_KEY.get(id));
                break;
            default:
                VMinusMod.LOGGER.warn("Vision type could not be found.");
                return null;
        }
        if (mainVision == null) {
            VMinusMod.LOGGER.warn("Main vision could not be found: " + id);
            return null;
        }
        JsonObject mergedData = new JsonObject();
        // merge all data
        for (String key : mainVision.keySet())
            mergedData = scanVisionKey(mainVision, key, id, mergedData, itemstack, block, entity);
        // cache any uncached data
        boolean validConditions;
        boolean wasNull = false;
        if (mergedData == null) {
            wasNull = true;
            mergedData = new JsonObject();
        }
        validConditions = VisionValueHelper.checkConditions(mergedData, itemstack, block, entity);
        //if (!mergedData.entrySet().isEmpty()) {
            if (itemstack != null) {
                cacheVision(ITEM_VISION_KEY, ITEM_VISION_CACHE, mergedData, id);
            } else if (block != null) {
                cacheVision(BLOCK_VISION_KEY, BLOCK_VISION_CACHE, mergedData, id);
            } else if (entity != null) {
                cacheVision(ENTITY_VISION_KEY, ENTITY_VISION_CACHE, mergedData, id);
            } else if (effect != null) {
                cacheVision(EFFECT_VISION_KEY, EFFECT_VISION_CACHE, mergedData, id);
            } else if (enchantment != null) {
                cacheVision(ENCHANTMENT_VISION_KEY, ENCHANTMENT_VISION_CACHE, mergedData, id);
            }
        //}
        if (!validConditions || wasNull) {
            return null;
        }

        return mergedData;
    }

    private static ResourceLocation getOrCreateResourceLocation(String tagNamespace) {
        return RESOURCE_LOCATION_CACHE.computeIfAbsent(tagNamespace, ResourceLocation::new);
    }

    private static boolean isItemTagged(ItemStack itemstack, String tag) {
        String tagNamespace = tag.substring(1);
        TagKey<Item> itemTag = ITEM_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> ItemTags.create(getOrCreateResourceLocation(ns)));
        return itemstack.is(itemTag);
    }

    private static boolean isBlockTagged(Block block, String tag) {
        String tagNamespace = tag.substring(1);
        BlockState blockstate = block.defaultBlockState();
        TagKey<Block> blockTag = BLOCK_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> BlockTags.create(getOrCreateResourceLocation(ns)));
        return blockstate.is(blockTag);
    }

    private static boolean isEntityTagged(Entity entity, String tag) {
        String tagNamespace = tag.substring(1);
        TagKey<EntityType<?>> entityTag = ENTITY_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> TagKey.create(Registries.ENTITY_TYPE, getOrCreateResourceLocation(ns)));
        return entity.getType().is(entityTag);
    }

    public static JsonObject mergeJsonObjects(@Nullable JsonObject target, JsonObject source) {
        //Merging similar json objects as to not cause overwriting / conflicts with mods that affect the same object
        if (target == null)
            target = new JsonObject();
        for (String key : source.keySet()) {
            JsonElement sourceElement = source.get(key);
            if (sourceElement.isJsonArray()) {
                JsonArray sourceArray = sourceElement.getAsJsonArray();
                JsonArray targetArray = target.has(key) ? target.getAsJsonArray(key) : new JsonArray();
                JsonArray mergedArray = mergeJsonArrays(targetArray, sourceArray);
                target.add(key, mergedArray);
            } else {
                target.add(key, sourceElement);
            }
        }
        return target;
    }

    private static JsonArray mergeJsonArrays(JsonArray targetArray, JsonArray sourceArray) {
        JsonArray mergedArray = new JsonArray();
        for (JsonElement element : targetArray) {
            mergedArray.add(element);
        }
        for (JsonElement element : sourceArray) {
            mergedArray.add(element);
        }
        return mergedArray;
    }
}
