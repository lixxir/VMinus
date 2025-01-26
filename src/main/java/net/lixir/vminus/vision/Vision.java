package net.lixir.vminus.vision;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class Vision {
    public static final int EMPTY_KEY = -1;

    public static final Map<String, SoundType> BLOCK_SOUND_TYPE_CACHE = new HashMap<>();
    public static final List<ItemTabData> ITEM_TAB_DATA = new ArrayList<>();


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
                    found = isItemTagged(item, new ResourceLocation(matchKey), context);
                } else if (object instanceof Block block) {
                    found = isBlockTagged(block, new ResourceLocation(matchKey), context);
                } else if (object instanceof EntityType<?> entityType) {
                    found = isEntityTagged(entityType, new ResourceLocation(matchKey), context);
                }
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

    public static @Nullable JsonObject getData(@Nullable Object target) {
        if (target == null) return null;

        if (target instanceof ItemStack _itemStack) {
            return getData(_itemStack);
        } else if (target instanceof Item _item) {
            return getData(_item);
        } else if (target instanceof Block _block) {
            return getData(_block);
        } else if (target instanceof Entity) {
            return getData(((Entity) target).getType());
        } else if (target instanceof EntityType<?> _entityType) {
            return getData(_entityType);
        }
        return null;
    }

    public static @Nullable JsonObject getData(@Nullable Item item) {
        return getData(item, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Item item, int key) {
        return getData(item, false, key);
    }

    public static @Nullable JsonObject getData(@Nullable Item item, @Nullable Boolean debug) {
        return getData(item, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Item item, @Nullable Boolean debug, int key) {
        if (item == null)
            return null;
        String id = attemptGetObjectId(item);
        if (id.isEmpty())
            return null;
        VisionType visionType = getVisionType(item);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, item, key);
    }



    public static @Nullable JsonObject getData(@Nullable ItemStack itemstack) {
        return getData(itemstack, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable ItemStack itemstack, int key) {
        return getData(itemstack, false, key);
    }

    public static @Nullable JsonObject getData(@Nullable ItemStack itemstack, @Nullable Boolean debug) {
        return getData(itemstack, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable ItemStack itemstack, @Nullable Boolean debug, int key) {
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

    public static @Nullable JsonObject getData(@Nullable Block block, int key) {
        return getData(block, false, key);
    }

    public static @Nullable JsonObject getData(@Nullable Block block) {
        return getData(block, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Block block, @Nullable Boolean debug) {
        return getData(block, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Block block, @Nullable Boolean debug, int key) {

        String id = attemptGetObjectId(block);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(block);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, block, key);
    }

    public static @Nullable JsonObject getData(@Nullable EntityType<?> entityType) {
        return getData(entityType, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable EntityType<?> entityType, @Nullable Boolean debug) {
        return getData(entityType, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable EntityType<?> entityType, @Nullable Boolean debug, int key) {
        String id = attemptGetObjectId(entityType);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(entityType);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, entityType, key);
    }

    public static @Nullable JsonObject getData(@Nullable MobEffect mobEffect) {
        return getData(mobEffect, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable MobEffect mobEffect, @Nullable Boolean debug) {
        return getData(mobEffect, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable MobEffect mobEffect, @Nullable Boolean debug, int key) {
        String id = attemptGetObjectId(mobEffect);
        if (id == null || id.isEmpty())
            return null;
        VisionType visionType = getVisionType(mobEffect);
        if (visionType == null)
            return null;
        return processVisionData(visionType, id, mobEffect, key);
    }

    public static @Nullable JsonObject getData(@Nullable Enchantment enchantment) {
        return getData(enchantment, false, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Enchantment enchantment, @Nullable Boolean debug) {
        return getData(enchantment, debug, -1);
    }

    public static @Nullable JsonObject getData(@Nullable Enchantment enchantment, @Nullable Boolean debug, int key) {
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

    private static @Nullable String attemptGetObjectId(@Nullable Item item) {
        if (item == null) {
            return null;
        }
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
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

    private static @Nullable VisionType getVisionType(@Nullable Item item) {
        if (item == null) {
            return null;
        }
        return VisionType.ITEM;
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
                    Vision.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString(), item, -1, context);
                }
            }
            case BLOCK -> {
                for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                    Vision.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString(), block, -1, context);
                }
            }
            case ENTITY -> {
                for (EntityType<?> entity : ForgeRegistries.ENTITY_TYPES.getValues()) {
                    Vision.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity)).toString(), entity, -1, context);
                }
            }
            case EFFECT -> {
                for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                    Vision.processVisionData(
                            visionType, Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(effect)).toString(), effect, -1, context);
                }
            }
            case ENCHANTMENT -> {
                for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
                    Vision.processVisionData(
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
        ConcurrentHashMap<String, Integer> visionKey = visionType.getVisionKey();
        if (visionCache.isEmpty())
            return null;
        if (visionKey.isEmpty())
            return null;
        if (visionKey.containsKey(index))
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


    private static boolean isItemTagged(Item item, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
          if (context == null)
              return false;

            TagKey<Item> itemTagKey = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), matchKey);
            Collection<Holder<Item>> tags = context.getTag(itemTagKey);

            if (tags == null || tags.isEmpty())
                return false;

            ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);

            if (itemKey == null)
                return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.ITEMS.getKey(holder.value());
            return Objects.equals(holderKey, itemKey);
        });

    }

    private static boolean isBlockTagged(Block block, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null)
            return false;

        TagKey<Block> blockTagKey = TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), matchKey);
        Collection<Holder<Block>> tags = context.getTag(blockTagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation blockKey = ForgeRegistries.BLOCKS.getKey(block);

        if (blockKey == null)
            return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.BLOCKS.getKey(holder.value());
            return Objects.equals(holderKey, blockKey);
        });
    }

    private static boolean isEntityTagged(EntityType<?> entityType, ResourceLocation matchKey, @Nullable ICondition.IContext context) {
        if (context == null)
            return false;

        TagKey<EntityType<?>> entityTagKey = TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), matchKey);
        Collection<Holder<EntityType<?>>> tags = context.getTag(entityTagKey);

        if (tags == null || tags.isEmpty())
            return false;

        ResourceLocation entityKey = ForgeRegistries.ENTITY_TYPES.getKey(entityType);

        if (entityKey == null)
            return false;

        return tags.stream().anyMatch(holder -> {
            ResourceLocation holderKey = ForgeRegistries.ENTITY_TYPES.getKey(holder.value());
            return Objects.equals(holderKey, entityKey);
        });
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
