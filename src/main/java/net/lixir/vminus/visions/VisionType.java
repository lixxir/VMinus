package net.lixir.vminus.visions;

public enum VisionType {
    ITEM((byte) 0,  "items", "item"),
    BLOCK((byte) 1, "blocks", "block"),
    ENTITY((byte) 2,  "entities", "entity"),
    EFFECT((byte) 3, "effects", "effect"),
    ENCHANTMENT((byte) 4, "enchantments", "enchantment"),
    CREATIVE_TAB((byte) 5, "creative_tabs", "creative_tab");

    private final byte id;
    private final String directory;
    private final String listName;
    private final String singleName;

    VisionType(byte id, String listName, String singleName) {
        this.id = id;
        this.directory = "visions/" + listName;
        this.listName = listName;
        this.singleName = singleName;
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
}