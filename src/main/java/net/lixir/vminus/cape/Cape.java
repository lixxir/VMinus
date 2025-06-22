package net.lixir.vminus.cape;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public enum Cape {
    BEEPER("beeper", VMinus.ID, "textures/cape/beeper.png", flags -> flags.isPatreon() || flags.isBooster() || flags.isDeveloper()),
    GHOST("ghost", VMinus.ID, "textures/cape/ghost.png", CapeAccounts.Flags::isBooster),
    MIMIC("mimic", VMinus.ID, "textures/cape/mimic.png", CapeAccounts.Flags::isPatreon),
    BEDROCK_GOLEM("bedrock_golem", VMinus.ID, "textures/cape/bedrock_golem.png", flags -> flags.isPatreon() && flags.isBooster()),
    PHOTON("photon", VMinus.ID, "textures/cape/photon.png", CapeAccounts.Flags::isContributor);

    private final String id;
    private final String namespace;
    private final String texturePath;
    private final Predicate<CapeAccounts.Flags> predicate;

    Cape(String id, String namespace, String texturePath, Predicate<CapeAccounts.Flags> predicate) {
        this.id = id;
        this.namespace = namespace;
        this.texturePath = texturePath;
        this.predicate = predicate;
    }

    public boolean isAllowedFor(CapeAccounts.Flags flags) {
        return predicate.test(flags);
    }

    public String getId() {
        return id;
    }

    public @NotNull ResourceLocation getTexture() {
        return new ResourceLocation(namespace, texturePath);
    }

    public static boolean ownsCape(Entity entity, String capeId) {
        if (entity instanceof Player player) {
            Cape cape = Cape.fromId(capeId);
            UUID uuid = player.getGameProfile().getId();
            String name = player.getGameProfile().getName();
            CapeAccounts.Flags flags = name.equals("Dev") ? CapeAccounts.fromUUID(CapeAccounts.DEV_UUID) : CapeAccounts.fromUUID(uuid);

            if (cape != null && flags != null) {
                return cape.isAllowedFor(flags);
            }

            return "default".equalsIgnoreCase(capeId);
        }
        return false;
    }

    public static @NotNull List<Cape> getAvailableCapes(Player player) {
        List<Cape> available = new ArrayList<>();
        for (Cape cape : values()) {
            if (ownsCape(player, cape.getId())) {
                available.add(cape);
            }
        }
        return available;
    }

    public static @Nullable ResourceLocation getCapeTexture(@NotNull Player player) {
        String capeId = player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .map(capability -> capability.cape_id)
                .orElse("");

        if (ownsCape(player, capeId)) {
            Cape cape = Cape.fromId(capeId);
            if (cape != null) {
                return cape.getTexture();
            }
        }
        return null;
    }

    public static @Nullable Cape fromId(String capeId) {
        for (Cape cape : values()) {
            if (cape.id.equalsIgnoreCase(capeId)) {
                return cape;
            }
        }
        return null;
    }
}
