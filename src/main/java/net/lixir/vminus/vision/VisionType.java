package net.lixir.vminus.vision;

import com.google.gson.JsonObject;
import net.lixir.vminus.network.VminusModVariables;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public enum VisionType {
    ITEM((byte) 0,  "items"),
    BLOCK((byte) 1, "blocks"),
    ENTITY((byte) 2,  "entities"),
    EFFECT((byte) 3, "effects"),
    ENCHANTMENT((byte) 4, "enchantments"),
    CREATIVE_TAB((byte) 5, "creative_tabs");

    private final ConcurrentHashMap<String, Integer> visionKey = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<JsonObject> visionCache = new CopyOnWriteArrayList<>();

    private final byte id;
    private final String directory;
    private final String listType;
    private JsonObject mainVision = new JsonObject();

    VisionType(byte id, String listType) {
        this.id = id;
        this.directory = "visions/" + listType;
        this.listType = listType;
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

    public void setMainVision(JsonObject visionData) {
        this.mainVision = visionData;
    }

    public JsonObject getMainVision() {
        return mainVision;
    }

    public static VisionType getFromDirectory(String folderName) {
        return switch (folderName) {
            case "visions/items" -> ITEM;
            case "visions/blocks" -> BLOCK;
            case "visions/entities" -> ENTITY;
            case "visions/effects" -> EFFECT;
            case  "visions/enchantments" -> ENCHANTMENT;
            case  "visions/creative_tabs" -> CREATIVE_TAB;
            default -> throw new IllegalStateException("Unexpected Folder Name: " + folderName);
        };
    }
}