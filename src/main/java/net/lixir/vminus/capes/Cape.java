package net.lixir.vminus.capes;

import net.minecraft.resources.ResourceLocation;

public enum Cape {
    BEEPER("beeper", "vminus", "textures/cape/beeper.png"),
    GHOST("ghost", "vminus", "textures/cape/ghost.png"),
    SHROUD("shroud", "vminus", "textures/cape/shroud.png"),
    MARROW("marrow", "vminus", "textures/cape/marrow.png"),
    PROTOTYPE("prototype", "vminus", "textures/cape/prototype.png"),
    TROLL("troll", "vminus", "textures/cape/troll.png"),
    PHOTON("photon", "vminus", "textures/cape/photon.png");

    private final String id;
    private final String namespace;
    private final String texturePath;

    Cape(String id, String namespace, String texturePath) {
        this.id = id;
        this.namespace = namespace;
        this.texturePath = texturePath;
    }

    public String getId() {
        return id;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(namespace, texturePath);
    }

    public static Cape fromString(String capeId) {
        for (Cape id : values()) {
            if (id.getId().equalsIgnoreCase(capeId)) {
                return id;
            }
        }
        return null;
    }

    public static Cape fromId(String capeId) {
        for (Cape id : values()) {
            if (id.getId().equalsIgnoreCase(capeId)) {
                return id;
            }
        }
        return null;
    }
}