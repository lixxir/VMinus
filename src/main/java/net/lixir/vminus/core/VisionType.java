package net.lixir.vminus.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.lixir.vminus.core.resources.deserializers.ItemVisionDeserializer;
import net.lixir.vminus.core.visions.ItemVision;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public enum VisionType {
    ITEM((byte) 0,  "items", "item", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create()),
    BLOCK((byte) 1, "blocks", "block", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create()),
    ENTITY((byte) 2,  "entities", "entity", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create()),
    EFFECT((byte) 3, "effects", "effect", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create()),
    ENCHANTMENT((byte) 4, "enchantments", "enchantment", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create()),
    CREATIVE_TAB((byte) 5, "creative_tabs", "creative_tab", new GsonBuilder().registerTypeAdapter(ItemVision.class, new ItemVisionDeserializer()).setPrettyPrinting().disableHtmlEscaping().create());

    private final ConcurrentHashMap<String, Integer> visionKey = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<JsonObject> visionCache = new CopyOnWriteArrayList<>();

    private final byte id;
    private final String directory;
    private final String listName;
    private final String singleName;
    private final Gson GSON;
    private JsonObject mainVision = new JsonObject();

    VisionType(byte id, String listName, String singleName, Gson gson) {
        this.id = id;
        this.directory = "visions/" + listName;
        this.listName = listName;
        this.singleName = singleName;
        this.GSON = gson;
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

    public String getListName() {
        return this.listName;
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

    public String getSingleName() {
        return singleName;
    }

    public Gson getGSON() {
        return GSON;
    }
}