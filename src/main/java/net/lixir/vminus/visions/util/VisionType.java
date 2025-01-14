package net.lixir.vminus.visions.util;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.VminusModVariables;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public enum VisionType {
    ITEM((byte) 0, "item_visions", "items"),
    BLOCK((byte) 1, "block_visions", "blocks"),
    ENTITY((byte) 2, "entity_visions", "entities"),
    EFFECT((byte) 3, "effect_visions", "effects"),
    ENCHANTMENT((byte) 4, "enchantment_visions", "enchantments");

    private final ConcurrentHashMap<String, Integer> visionKey = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<JsonObject> visionCache = new CopyOnWriteArrayList<>();

    private final byte id;
    private final String directory;
    private final String listType;

    VisionType(byte id, String directory, String listType) {
        this.id = id;
        this.directory = directory;
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
        return this.directory;
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