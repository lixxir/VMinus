package net.lixir.vminus.visions.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public enum VisionType {
    ITEM(VisionHandler.getItemVisionKey(), VisionHandler.getItemVisionCache(), (byte) 0, "item_visions", "items"),
    BLOCK(VisionHandler.getBlockVisionKey(), VisionHandler.getBlockVisionCache(), (byte) 1, "block_visions", "blocks"),
    ENTITY(VisionHandler.getEntityVisionKey(), VisionHandler.getEntityVisionCache(), (byte) 2, "entity_visions", "entities"),
    EFFECT(VisionHandler.getEffectVisionKey(), VisionHandler.getEffectVisionCache(), (byte) 3, "effect_visions", "effects"),
    ENCHANTMENT(VisionHandler.getEnchantmentVisionKey(), VisionHandler.getEnchantmentVisionCache(), (byte) 4, "enchantment_visions", "enchantments");

    private final ConcurrentHashMap<String, Integer> visionKey;
    private final CopyOnWriteArrayList<JsonObject> visionCache;
    private final byte id;
    private final String folderName;
    private final String listType;

    VisionType(ConcurrentHashMap<String, Integer> visionKey, CopyOnWriteArrayList<JsonObject> visionCache, byte id, String folderName, String listType) {
        this.visionKey = visionKey;
        this.visionCache = visionCache;
        this.id = id;
        this.folderName = folderName;
        this.listType = listType;
    }

    public @Nullable JsonObject getMainVision() {
        switch(id) {
            case 0 -> {
                return VminusModVariables.main_item_vision;
            }
            case 1 -> {
                return VminusModVariables.main_block_vision;
            }
            case 2 -> {
                return VminusModVariables.main_entity_vision;
            }
            case 3 -> {
                return VminusModVariables.main_effect_vision;
            }
            case 4 -> {
                return VminusModVariables.main_enchantment_vision;
            }
        }
        return null;
    }

    public ConcurrentHashMap<String, Integer> getVisionKey() {
        return this.visionKey;
    }

    public CopyOnWriteArrayList<JsonObject> getVisionCache() {
        return this.visionCache;
    }

    public byte getId() {
        return this.id;
    }

    public String getDirectoryName() {
        return this.folderName;
    }

    public String getListType() {
        return this.listType;
    }

    public static VisionType getFromDirectory(String folderName) {
        return switch (folderName) {
            case "item_visions" -> ITEM;
            case "block_visions" -> BLOCK;
            case "entity_visions" -> ENTITY;
            case "effect_visions" -> EFFECT;
            case  "enchantment_visions" -> ENCHANTMENT;
            default -> throw new IllegalStateException("Unexpected Folder Name: " + folderName);
        };
    }

    public void setMainVision(JsonObject jsonObject) {
        switch (id) {
            case 0 -> VminusModVariables.main_item_vision = jsonObject;
            case 1 -> VminusModVariables.main_block_vision = jsonObject;
            case 2 -> VminusModVariables.main_entity_vision = jsonObject;
            case 3 -> VminusModVariables.main_effect_vision = jsonObject;
            case 4 -> VminusModVariables.main_enchantment_vision = jsonObject;
        }
    }

    public void clearMainVision() {
        switch (id) {
            case 0 -> VminusModVariables.main_item_vision = new JsonObject();
            case 1 -> VminusModVariables.main_block_vision = new JsonObject();
            case 2 -> VminusModVariables.main_entity_vision = new JsonObject();
            case 3 -> VminusModVariables.main_effect_vision = new JsonObject();
            case 4 -> VminusModVariables.main_enchantment_vision = new JsonObject();
        }
    }

}