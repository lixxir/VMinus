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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class VisionHandler {
    public static final byte ITEM_TYPE = 0;
    public static final byte BLOCK_TYPE = 1;
    public static final byte ENTITY_TYPE = 2;
    public static final byte EFFECT_TYPE = 3;
    public static final byte ENCHANTMENT_TYPE = 4;
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

    public static ConcurrentHashMap<String, Integer> getBlockVisionKey() {
        return BLOCK_VISION_KEY;
    }

    public static CopyOnWriteArrayList<JsonObject> getBlockVisionCache() {
        return BLOCK_VISION_CACHE;
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

    public static JsonObject getVisionData(EntityType<?> entityType, Boolean debug) {
        return getVisionData(null, debug, null, entityType, null, null);
    }

    public static JsonObject getVisionData(EntityType<?> entityType) {
        return getVisionData(null, false, null, entityType, null, null);
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


    // Clears the caches and their associated keys
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

    /**
     * @param mainVision The combined cached file of every resource file of that specific vision type.
     * @param key        The key identifier of the passed vision.
     * @param id         The id of the passed.
     * @param itemstack  An optionally passed itemstack.
     * @param block      An optionally passed block.
     * @param entityType An optionally passed entity type.
     * @return Merged vision or initially provided vision dependent on if the id matches correctly.
     * <p>
     * <br></br>
     * Scans all visions from the main vision to see if the provided id matches with the provided vision key.
     * </p>
     */
    private static JsonObject scanVisionKey(JsonObject mainVision, String key, String id, JsonObject mergedData,
                                            @Nullable ItemStack itemstack, @Nullable Block block, @Nullable EntityType<?> entityType) {
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

            if (matchKey.equals(id) || matchKey.equals("global")) {
                found = true;
            } else if (isTag) {
                found = (itemstack != null && isItemTagged(itemstack, matchKey)) ||
                        (block != null && isBlockTagged(block, matchKey)) ||
                        (entityType != null && isEntityTagged(entityType, matchKey));
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


    public static JsonObject getVisionData(@Nullable ItemStack itemstack, @Nullable Boolean debug, @Nullable Block block, @Nullable EntityType<?> entityType, @Nullable MobEffect effect, @Nullable Enchantment enchantment) {
        String id = null;
        byte type = -1;

        if (debug) {
            VMinusMod.LOGGER.info("______________DEBUGGING______________");
        }

        if (itemstack != null) {
            type = ITEM_TYPE;
            id = itemstack.hasTag() && itemstack.getOrCreateTag().contains("vision")
                    ? itemstack.getOrCreateTag().getString("vision")
                    : ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString();
        } else if (block != null) {
            type = BLOCK_TYPE;
            id = ForgeRegistries.BLOCKS.getKey(block).toString();
        } else if (entityType != null) {
            type = ENTITY_TYPE;
            id = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();
        } else if (effect != null) {
            type = EFFECT_TYPE;
            id = ForgeRegistries.MOB_EFFECTS.getKey(effect).toString();
        } else if (enchantment != null) {
            type = ENCHANTMENT_TYPE;
            id = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
        }

        if (type == -1 || id == null) {
            return null;
        }

        ConcurrentHashMap<String, Integer> visionKeyMap;
        CopyOnWriteArrayList<JsonObject> visionCache;

        switch (type) {
            case ITEM_TYPE -> {
                visionKeyMap = ITEM_VISION_KEY;
                visionCache = ITEM_VISION_CACHE;
            }
            case BLOCK_TYPE -> {
                visionKeyMap = BLOCK_VISION_KEY;
                visionCache = BLOCK_VISION_CACHE;
            }
            case ENTITY_TYPE -> {
                visionKeyMap = ENTITY_VISION_KEY;
                visionCache = ENTITY_VISION_CACHE;
            }
            case EFFECT_TYPE -> {
                visionKeyMap = EFFECT_VISION_KEY;
                visionCache = EFFECT_VISION_CACHE;
            }
            case ENCHANTMENT_TYPE -> {
                visionKeyMap = ENCHANTMENT_VISION_KEY;
                visionCache = ENCHANTMENT_VISION_CACHE;
            }
            default -> {
                VMinusMod.LOGGER.warn("Vision type could not be found.");
                return null;
            }
        }

        JsonObject mainVision = getMainVisionByType(type);
        JsonObject mergedData = new JsonObject();
        for (String key : mainVision.keySet()) {
            mergedData = scanVisionKey(mainVision, key, id, mergedData, itemstack, block, entityType);
        }


        int existingIndex = -1;
        for (int i = 0; i < visionCache.size(); i++) {
            if (visionCache.get(i).equals(mergedData)) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex != -1) {
            visionKeyMap.put(id, existingIndex);
            return visionCache.get(existingIndex);
        } else {
            int newIndex = visionCache.size();
            visionKeyMap.put(id, newIndex);
            visionCache.add(mergedData);
            return mergedData;
        }
    }


    private static JsonObject getMainVisionByType(byte type) {
        return switch (type) {
            case ITEM_TYPE -> VminusModVariables.main_item_vision;
            case BLOCK_TYPE -> VminusModVariables.main_block_vision;
            case ENTITY_TYPE -> VminusModVariables.main_entity_vision;
            case EFFECT_TYPE -> VminusModVariables.main_effect_vision;
            case ENCHANTMENT_TYPE -> VminusModVariables.main_enchantment_vision;
            default -> null;
        };
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

    private static boolean isEntityTagged(EntityType<?> entity, String tag) {
        if (!tag.startsWith("#")) {
            return false;
        }
        String tagNamespace = tag.substring(1);
        TagKey<EntityType<?>> entityTag = ENTITY_TAG_CACHE.computeIfAbsent(tagNamespace, ns -> TagKey.create(Registries.ENTITY_TYPE, getOrCreateResourceLocation(ns)));
        return entity.is(entityTag);
    }


    public static JsonObject mergeJsonObjects(@Nullable JsonObject target, JsonObject source) {
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
