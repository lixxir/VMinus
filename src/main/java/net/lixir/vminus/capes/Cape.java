package net.lixir.vminus.capes;

import net.lixir.vminus.VMinus;
import net.minecraft.resources.ResourceLocation;

public enum Cape {
    BEEPER("beeper", VMinus.ID, "textures/cape/beeper.png"),
    GHOST("ghost", VMinus.ID, "textures/cape/ghost.png"),
    SHROUD("shroud", VMinus.ID, "textures/cape/shroud.png"),
    MARROW("marrow", VMinus.ID, "textures/cape/marrow.png"),
    PROTOTYPE("prototype", VMinus.ID, "textures/cape/prototype.png"),
    TROLL("troll", VMinus.ID, "textures/cape/troll.png"),
    PHOTON("photon", VMinus.ID, "textures/cape/photon.png");

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