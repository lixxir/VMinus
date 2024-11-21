package net.lixir.vminus.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.lixir.vminus.VminusMod;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber
public class VisionHandler {
    // json caches
    private static final ConcurrentHashMap<String, Integer> ITEM_VISION_KEY = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<JsonObject> ITEM_VISION_CACHE = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<String, JsonObject> BLOCK_VISION_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, JsonObject> ENTITY_VISION_CACHE = new ConcurrentHashMap<String, JsonObject>();
    private static final ConcurrentHashMap<String, JsonObject> EFFECT_VISION_CACHE = new ConcurrentHashMap<String, JsonObject>();
    private static final ConcurrentHashMap<String, JsonObject> ENCHANTMENT_VISION_CACHE = new ConcurrentHashMap<String, JsonObject>();
    // tag & resource location caches
    private static final ConcurrentHashMap<String, ResourceLocation> resourceLocationCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<EntityType<?>>> entityTagCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<Block>> blockTagCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, TagKey<Item>> itemTagCache = new ConcurrentHashMap<>();
    public static final int ITEM_TYPE = 0;

    public static ConcurrentHashMap<String, Integer> getItemVisionKey() {
        return ITEM_VISION_KEY;
    }

    public static CopyOnWriteArrayList<JsonObject> getItemVisionCache() {
        return ITEM_VISION_CACHE;
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

    private static void clearCaches() {
        ITEM_VISION_KEY.clear();
        ITEM_VISION_CACHE.clear();
        /*
        BLOCK_VISION_CACHE.clear();
        ENTITY_VISION_CACHE.clear();
        EFFECT_VISION_CACHE.clear();
        ENCHANTMENT_VISION_CACHE.clear();

         */
    }

    @SubscribeEvent
    public static void onWorldLoad(net.minecraftforge.event.level.LevelEvent.Load event) {
        if (VminusModVariables.main_item_vision != null) {
			for (Item item : ForgeRegistries.ITEMS.getValues()) {
				ItemStack itemStack = new ItemStack(item);
				getVisionData(itemStack);
			}
		}
		// VminusMod.LOGGER.info("PRE-LOADED CACHE:" + itemVisionKey);
        /*
		if (VminusModVariables.main_block_vision != null) {
			for (Block block : ForgeRegistries.BLOCKS.getValues()) {
				getVisionData(block);
			}
		}
		// VminusMod.LOGGER.info("PRE-LOADED CACHE:" + blockVisionKey);
		if (VminusModVariables.main_effect_vision != null) {
			for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
				getVisionData(effect);
			}
		}
		// VminusMod.LOGGER.info("PRE-LOADED CACHE:" + effectVisionKey);
		if (VminusModVariables.main_enchantment_vision != null) {
			for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
				getVisionData(enchantment);
			}
		}
	    //Entities are not preloaded due to crashes of not having a world to generate the entity in.
         */
		//VminusMod.LOGGER.info("PRE-LOADED KEY:" + ITEM_VISION_KEY);
        //VminusMod.LOGGER.info("PRE-LOADED CACHE:" + ITEM_VISION_CACHE);
    }

    public static JsonObject getVisionData(@Nullable ItemStack itemstack, @Nullable Boolean debug, @Nullable Block block, @Nullable Entity entity, @Nullable MobEffect effect, @Nullable Enchantment enchantment) {
        JsonObject mainVision = null;
        String id = "";
        byte type = -1;
        //getting the registery ids of the different objects
        if (debug)
            VminusMod.LOGGER.info("______________DEBUGGING______________");
        if (itemstack != null)
            type = ITEM_TYPE;
        if (block != null)
            type = 1;
        if (entity != null)
            type = 2;
        if (effect != null)
            type = 3;
        if (enchantment != null) {
            if (debug)
                VminusMod.LOGGER.info("Enchantment is not null: " + enchantment);
            type = 4;
        }
        if (debug)
            VminusMod.LOGGER.info("Type: " + type);
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
            case 1:
                // blocks
                mainVision = VminusModVariables.main_block_vision;
                id = ForgeRegistries.BLOCKS.getKey(block).toString();
                //if (blockVisionKey.containsKey(id))
                //	return blockVisionKey.get(id);
                break;
            case 2:
                // entities
                mainVision = VminusModVariables.main_entity_vision;
                id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
                //if (entityVisionKey.containsKey(id))
                //	return entityVisionKey.get(id);
                break;
            case 3:
                // effects
                mainVision = VminusModVariables.main_effect_vision;
                id = ForgeRegistries.MOB_EFFECTS.getKey(effect).toString();
                //if (effectVisionKey.containsKey(id))
                //	return effectVisionKey.get(id);
                break;
            case 4:
                // enchantments
                mainVision = VminusModVariables.main_enchantment_vision;
                if (debug)
                    VminusMod.LOGGER.info("Found main enchantment vision: " + mainVision);
                id = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
                if (debug)
                    VminusMod.LOGGER.info("Found enchantment id: " + id);
				/*
								if (enchantmentVisionKey.containsKey(id)) {
									if (debug == true)
										VminusMod.LOGGER.info("Enchantment Vision Key has: " + id);
									return enchantmentVisionKey.get(id);
								}
				*/
                break;
            default:
                VminusMod.LOGGER.warn("Vision type could not be found.");
                return null;
        }
        if (mainVision == null) {
            VminusMod.LOGGER.warn("Main vision could not be found: " + id);
            return null;
        }
        // checks the caches to see if the json object was already merged and added before
        JsonObject mergedData = new JsonObject();
        // merge id-specific data
        if (mainVision.has(id)) {
            if (debug)
                VminusMod.LOGGER.info("Main vision had id: " + id);
            JsonObject visionData = mainVision.getAsJsonObject(id);
            mergeJsonObjects(mergedData, visionData);
        }
        // merge tag-based data
        switch (type) {
            case ITEM_TYPE:
                // items tag
                for (String key : mainVision.keySet()) {
                    if (key.startsWith("#") && isItemTagged(itemstack, key)) {
                        JsonObject tagData = mainVision.getAsJsonObject(key);
                        mergedData = mergeJsonObjects(mergedData, tagData);
                    }
                }
                break;
            case 1:
                // blocks tags
                for (String key : mainVision.keySet()) {
                    if (key.startsWith("#") && isBlockTagged(block, key)) {
                        JsonObject tagData = mainVision.getAsJsonObject(key);
                        mergedData = mergeJsonObjects(mergedData, tagData);
                    }
                }
                break;
            case 2:
                // entities type tags
                for (String key : mainVision.keySet()) {
                    if (key.startsWith("#") && isEntityTagged(entity, key)) {
                        JsonObject tagData = mainVision.getAsJsonObject(key);
                        mergedData = mergeJsonObjects(mergedData, tagData);
                    }
                }
                break;
        }
        // merging globally applied data
        for (String key : mainVision.keySet()) {
            if (key.equals("global")) {
                JsonObject globalData = mainVision.getAsJsonObject(key);
                mergedData = mergeJsonObjects(mergedData, globalData);
            }
        }
        if (mergedData != null && !mergedData.entrySet().isEmpty()) {
            if (itemstack != null) {
                int index = ITEM_VISION_CACHE.indexOf(mergedData);
                if (index == -1) {
                    ITEM_VISION_CACHE.add(mergedData);
                    index = ITEM_VISION_CACHE.size() - 1;
                }
                if (!ITEM_VISION_KEY.containsKey(id))
                    ITEM_VISION_KEY.put(id, index);
            }
        }

        if (block != null && !BLOCK_VISION_CACHE.containsKey(id))
            BLOCK_VISION_CACHE.put(id, mergedData);

        if (entity != null && !ENTITY_VISION_CACHE.containsKey(id))
            ENTITY_VISION_CACHE.put(id, mergedData);

        if (effect != null && !EFFECT_VISION_CACHE.containsKey(id))
            EFFECT_VISION_CACHE.put(id, mergedData);

        if (enchantment != null && !ENCHANTMENT_VISION_CACHE.containsKey(id)) {
            if (debug)
                VminusMod.LOGGER.info("Enchantment vision key adding: " + id);
            ENCHANTMENT_VISION_CACHE.put(id, mergedData);
        }
        if (mergedData.entrySet().isEmpty()) {
            if (debug)
                VminusMod.LOGGER.warn("Merged data entry set is empty: " + id);
            return null;
        }
        return mergedData;
    }

    private static ResourceLocation getOrCreateResourceLocation(String tagNamespace) {
        return resourceLocationCache.computeIfAbsent(tagNamespace, ResourceLocation::new);
    }

    private static boolean isItemTagged(ItemStack itemstack, String tag) {
        String tagNamespace = tag.substring(1);
        TagKey<Item> itemTag = itemTagCache.computeIfAbsent(tagNamespace, ns -> ItemTags.create(getOrCreateResourceLocation(ns)));
        return itemstack.is(itemTag);
    }

    private static boolean isBlockTagged(Block block, String tag) {
        String tagNamespace = tag.substring(1);
        BlockState blockstate = block.defaultBlockState();
        TagKey<Block> blockTag = blockTagCache.computeIfAbsent(tagNamespace, ns -> BlockTags.create(getOrCreateResourceLocation(ns)));
        return blockstate.is(blockTag);
    }

    private static boolean isEntityTagged(Entity entity, String tag) {
        String tagNamespace = tag.substring(1);
        TagKey<EntityType<?>> entityTag = entityTagCache.computeIfAbsent(tagNamespace, ns -> TagKey.create(Registries.ENTITY_TYPE, getOrCreateResourceLocation(ns)));
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
