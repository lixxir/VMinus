package net.lixir.vminus.visions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.util.VisionType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class VisionHandler {
    public static final int EMPTY_KEY = -1;

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

    public static CopyOnWriteArrayList<JsonObject> getItemVisionCache() {
        return ITEM_VISION_CACHE;
    }

    public static CopyOnWriteArrayList<JsonObject> getBlockVisionCache() {
        return BLOCK_VISION_CACHE;
    }

    public static CopyOnWriteArrayList<JsonObject> getEntityVisionCache() {
        return ENTITY_VISION_CACHE;
    }

    public static CopyOnWriteArrayList<JsonObject> getEffectVisionCache() {
        return EFFECT_VISION_CACHE;
    }

    public static CopyOnWriteArrayList<JsonObject> getEnchantmentVisionCache() {
        return ENCHANTMENT_VISION_CACHE;
    }

    public static ConcurrentHashMap<String, Integer> getItemVisionKey() {
        return ITEM_VISION_KEY;
    }

    public static ConcurrentHashMap<String, Integer> getBlockVisionKey() {
        return BLOCK_VISION_KEY;
    }

    public static ConcurrentHashMap<String, Integer> getEntityVisionKey() {
        return ENTITY_VISION_KEY;
    }

    public static ConcurrentHashMap<String, Integer> getEffectVisionKey() {
        return EFFECT_VISION_KEY;
    }

    public static ConcurrentHashMap<String, Integer> getEnchantmentVisionKey() {
        return ENCHANTMENT_VISION_KEY;
    }

    public static void loadVisions() {
        /*
        if (VminusModVariables.main_item_vision != null) {
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                ItemStack itemStack = new ItemStack(item);
                VisionHandler.getVisionData(itemStack);
            }
        }

         */
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
        if (VminusModVariables.main_enchantment_vision != null) {
            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                VisionHandler.getVisionData(enchantment);
            }
        }
        //Entities are not preloaded due to errors.
    }


    // Clears the caches and their associated keys
    public static void clearCaches() {
        VisionType.ITEM.getVisionKey().clear();
        VisionType.ITEM.getVisionCache().clear();

        VisionType.BLOCK.getVisionKey().clear();
        VisionType.BLOCK.getVisionCache().clear();

        VisionType.ENTITY.getVisionKey().clear();
        VisionType.ENTITY.getVisionCache().clear();

        VisionType.EFFECT.getVisionKey().clear();
        VisionType.EFFECT.getVisionCache().clear();

        VisionType.ENCHANTMENT.getVisionKey().clear();
        VisionType.ENCHANTMENT.getVisionCache().clear();
    }

    private static JsonObject scanVisionJsonKey(JsonObject mainVision, String key, String id, JsonObject mergedData,
                                                @Nullable Object object, @Nullable ICondition.IContext context) {
        final String originalKey = key;
        key = key.trim();

        List<String> parts = processKeyString(key);
        boolean invalidMatch = false;
        boolean validMatchFound = false;

        for (String matchKey : parts) {
            boolean inverted = matchKey.startsWith("!");
            if (inverted) matchKey = matchKey.substring(1);

            boolean found = false;
            boolean isTag = matchKey.startsWith("#");
            if (isTag)
                matchKey = matchKey.substring(1);

            if (matchKey.equals(id) || matchKey.equals("global")) {
                found = true;
            } else if (isTag) {
                if (object instanceof Item item) {
                    found = isItemTaggedUsingContext(item, new ResourceLocation(matchKey), context);
                } else if (object instanceof Block block) {
                    found = isBlockTagged(block, matchKey);
                } else if (object instanceof EntityType<?> entityType) {
                    found = isEntityTagged(entityType, matchKey);
                }
                if (found) VMinusMod.LOGGER.info("ID is tagged: {}", id);
            }

            if (inverted) found = !found;

            if (!inverted && found) {
                validMatchFound = true;
            } else if (inverted && !found) {
                invalidMatch = true;
                break;
            }
        }

        if (invalidMatch || !validMatchFound) {
            return mergedData;
        }

        JsonObject matchedData = mainVision.getAsJsonObject(originalKey);
        if (matchedData != null) {
            return mergeJsonObjects(mergedData, matchedData);
        }

        return mergedData;
    }

    private static List<String> processKeyString(String key) {
        key = key.trim();
        if (key.endsWith(",")) {
            key = key.substring(0, key.length() - 1);
        }
        return Arrays.stream(key.split(","))
                .map(String::trim)
                .toList();
    }

    public static @Nullable JsonObject getVisionData(@Nullable ItemStack itemstack) {
        return getVisionData(itemstack, false, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable ItemStack itemstack, int key) {
        return getVisionData(itemstack, false, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable ItemStack itemstack, @Nullable Boolean debug) {
        return getVisionData(itemstack, debug, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable ItemStack itemstack, @Nullable Boolean debug, int key) {
        if (itemstack == null)
            return null;
        String id = attemptGetObjectId(itemstack);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(itemstack);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, itemstack, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Block block, int key) {
        return getVisionData(block, false, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Block block) {
        return getVisionData(block, false, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Block block, @Nullable Boolean debug) {
        return getVisionData(block, debug, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Block block, @Nullable Boolean debug, int key) {

        String id = attemptGetObjectId(block);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(block);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, block, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable EntityType<?> entityType) {
        return getVisionData(entityType, false, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable EntityType<?> entityType, @Nullable Boolean debug) {
        return getVisionData(entityType, debug, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable EntityType<?> entityType, @Nullable Boolean debug, int key) {
        String id = attemptGetObjectId(entityType);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(entityType);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, entityType, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable MobEffect mobEffect) {
        return getVisionData(mobEffect, false, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable MobEffect mobEffect, @Nullable Boolean debug) {
        return getVisionData(mobEffect, debug, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable MobEffect mobEffect, @Nullable Boolean debug, int key) {
        String id = attemptGetObjectId(mobEffect);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(mobEffect);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, mobEffect, key);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Enchantment enchantment) {
        return getVisionData(enchantment, false, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Enchantment enchantment, @Nullable Boolean debug) {
        return getVisionData(enchantment, debug, -1);
    }

    public static @Nullable JsonObject getVisionData(@Nullable Enchantment enchantment, @Nullable Boolean debug, int key) {
        String id = attemptGetObjectId(enchantment);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(enchantment);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, enchantment, key);
    }

    private static @Nullable String attemptGetObjectId(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (itemStack.hasTag() && itemStack.getOrCreateTag().contains("vision")) {
            return itemStack.getOrCreateTag().getString("vision");
        }
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString();
    }

    private static @Nullable String attemptGetObjectId(@Nullable Block block) {
        if (block == null) {
            return null;
        }
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString();
    }

    private static @Nullable String attemptGetObjectId(@Nullable EntityType<?> entityType) {
        if (entityType == null) {
            return null;
        }
        return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType)).toString();
    }

    private static @Nullable String attemptGetObjectId(@Nullable MobEffect mobEffect) {
        if (mobEffect == null) {
            return null;
        }
        return Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(mobEffect)).toString();
    }

    private static @Nullable String attemptGetObjectId(@Nullable Enchantment enchantment) {
        if (enchantment == null) {
            return null;
        }
        return Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString();
    }


    private static @Nullable VisionType getVisionType(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        return VisionType.ITEM;
    }

    private static @Nullable VisionType getVisionType(@Nullable Block block) {
        if (block == null) {
            return null;
        }
        return VisionType.BLOCK;
    }

    private static @Nullable VisionType getVisionType(@Nullable EntityType<?> entityType) {
        if (entityType == null) {
            return null;
        }
        return VisionType.ENTITY;
    }

    private static @Nullable VisionType getVisionType(@Nullable MobEffect mobEffect) {
        if (mobEffect == null) {
            return null;
        }
        return VisionType.EFFECT;
    }

    private static @Nullable VisionType getVisionType(@Nullable Enchantment enchantment) {
        if (enchantment == null) {
            return null;
        }
        return VisionType.ENCHANTMENT;
    }

    public static void processAllVisionDataForType(VisionType visionType, @Nullable ICondition.IContext context) {
        switch (visionType) {
            case ITEM -> {
                for (Item item : ForgeRegistries.ITEMS.getValues()) {
                    VisionHandler.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString(), item, -1, context);
                }
            }
            case BLOCK -> {
                for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                    VisionHandler.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString(), block, -1, context);
                }
            }
            case ENTITY -> {
                for (EntityType<?> entity : ForgeRegistries.ENTITY_TYPES.getValues()) {
                    VisionHandler.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity)).toString(), entity, -1, context);
                }
            }
            case EFFECT -> {
                for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                    VisionHandler.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(effect)).toString(), effect, -1, context);
                }
            }
            case ENCHANTMENT -> {
                for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                    VisionHandler.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString(), enchantment, -1, context);
                }
            }
        }
    }



        public static JsonObject processVisionData(VisionType visionType, String id, @Nullable Object object, int key) {
        return processVisionData(visionType, id, object, key, null);
    }


    public static JsonObject processVisionData(VisionType visionType, String id, @Nullable Object object, int key,
                                               @Nullable ICondition.IContext context) {
        JsonObject objectFromKey = getCacheByKey(key, visionType);
        if (objectFromKey != null)
            return objectFromKey;

        AbstractMap<String, Integer> visionKeyMap = visionType.getVisionKey();
        CopyOnWriteArrayList<JsonObject> visionCache = visionType.getVisionCache();
        JsonObject mainVision = visionType.getMainVision();

        if (mainVision == null) return null;

        /* Checking if the id is already found, if not then give -1 as the index.
            If it is not -1 then it can attempt to find a cache using that key
         */
        int index = visionKeyMap.getOrDefault(id, -1);
        JsonObject cachedJsonObject = getCacheByKey(index, visionType);
        if (cachedJsonObject != null) return cachedJsonObject;

        /* Scans through the main vision and processes everything
            to figure out what applies to the current object using the given registry id.
         */
        JsonObject jsonObject = new JsonObject();
        for (String mainVisionKey : mainVision.keySet())
            jsonObject = scanVisionJsonKey(mainVision, mainVisionKey, id, jsonObject, object, context);


        // Try to find a json object with the same json to use the same keys for condensing storage of variables
        index = getKeyFromMatchingCache(visionCache, jsonObject);
        if (index != -1) {
            visionKeyMap.put(id, index);
            return visionCache.get(index);
        }

        // If nothing was found, then cache the json and return the final product
        cacheVisionData(jsonObject, visionKeyMap, visionCache, id);
        return jsonObject;
    }

    public static int getCacheKey(@Nullable EntityType<?> entityType) {
        return getCacheKey(getVisionType(entityType), attemptGetObjectId(entityType));
    }

    public static int getCacheKey(@Nullable ItemStack itemStack) {
        return getCacheKey(getVisionType(itemStack), attemptGetObjectId(itemStack));
    }

    public static int getCacheKey(@Nullable Block block) {
        return getCacheKey(getVisionType(block), attemptGetObjectId(block));
    }

    public static int getCacheKey(VisionType visionType, String id) {
        if (visionType == null || id == null)
            return -1;
        AbstractMap<String, Integer> visionKeyMap = visionType.getVisionKey();
        return visionKeyMap.getOrDefault(id, -1);
    }

    // Gets a JsonObject from the respective cache using the given index
    public static @Nullable JsonObject getCacheByKey(int index, VisionType visionType) {
        if (index == -1)
            return null;
        CopyOnWriteArrayList<JsonObject> visionCache = visionType.getVisionCache();
        if (visionCache.isEmpty())
            return null;
        return visionCache.get(index);
    }

    // Attempts to look for any matching JsonObject in the cache, and if it exists, then get the index
    public static int getKeyFromMatchingCache(CopyOnWriteArrayList<JsonObject> visionCache, JsonObject checkingObject) {
        for (int i = 0; i < visionCache.size(); i++) {
            if (visionCache.get(i).equals(checkingObject)) {
                return i;
            }
        }
        return -1;
    }

    private static void cacheVisionData(JsonObject jsonObject, AbstractMap<String, Integer> visionKeyMap, CopyOnWriteArrayList<JsonObject> visionCache, String id) {
        int newIndex = visionCache.size();
        visionKeyMap.put(id, newIndex);
        if (jsonObject == null)
            jsonObject = new JsonObject();
        visionCache.add(jsonObject);
    }


    private static ResourceLocation getOrCreateResourceLocation(String tagNamespace) {
        return RESOURCE_LOCATION_CACHE.computeIfAbsent(tagNamespace, ResourceLocation::new);
    }


    private static boolean isItemTaggedUsingContext(Item item, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null) {
            VMinusMod.LOGGER.warn("Context is null, cannot check tags.");
            return false;
        }


            TagKey<Item> itemTagKey = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), matchKey);
            Collection<Holder<Item>> tags = context.getTag(itemTagKey);

            if (tags == null || tags.isEmpty()) {
                VMinusMod.LOGGER.info("No tags found for key: " + matchKey);
                return false;
            }

            tags.forEach(tag -> VMinusMod.LOGGER.info("Found tag: " + ForgeRegistries.ITEMS.getKey(tag.value())));

            ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
            if (itemKey == null) {
                VMinusMod.LOGGER.warn("Item key is null for item: {}", item);
                return false;
            }
            boolean isTagged = tags.stream().anyMatch(holder -> {
                ResourceLocation holderKey = ForgeRegistries.ITEMS.getKey(holder.value());
                VMinusMod.LOGGER.info("Checking: " + holderKey + " against " + itemKey);
                return Objects.equals(holderKey, itemKey);
            });

            VMinusMod.LOGGER.info("Item " + itemKey + " tagged with " + matchKey + ": " + isTagged);
            return isTagged;


    }



    private static boolean isBlockTagged(Block block, String tag) {
        String tagNamespace = tag.substring(1);
        BlockState blockstate = block.defaultBlockState();
        TagKey<Block> blockTag = BLOCK_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> BlockTags.create(getOrCreateResourceLocation(ns)));
        return blockstate.is(blockTag);
    }

    private static boolean isEntityTagged(EntityType<?> entity, String tag) {
        if (!tag.startsWith("#")) {
            return false;
        }
        String tagNamespace = tag.substring(1);
        TagKey<EntityType<?>> entityTag = ENTITY_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), getOrCreateResourceLocation(ns)));
        return entity.is(entityTag);
    }

    private static JsonObject mergeJsonObjects(@Nullable JsonObject target, JsonObject source) {
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
