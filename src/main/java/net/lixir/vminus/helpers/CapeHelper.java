package net.lixir.vminus.helpers;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.List;

public class CapeHelper {
    //Name lists
    private static final List<String> PATREONS = Arrays.asList(
            "lixir_guy",
            "Sweetygamer2",
            "Dev",
            "SlimeSlabs",
            "Ultraman001",
            "Booneraniaro",
            "rosymaplemoss",
            "HoeNail",
            "ClephLeSDF",
            "BrianIsBro",
            "IzzyBizzy45");
    private static final List<String> BOOSTERS = Arrays.asList(
            "lixir_guy",
            "Jackdedestroyer",
            "KreloX",
            "Dev",
            "_Ajgor_",
            "Azvalen",
            "Goongamer77",
            "MooreGaming1324",
            "Meme___Man",
            "reindawn",
            "Simonisnear",
            "miloq__",
            "Stallman1111");
    private static final List<String> DEVELOPERS = Arrays.asList(
            "lixir_guy",
            "Jackdedestroyer",
            "hhumanoid",
            "Dev");
    private static final List<String> CONTRIBUTORS = Arrays.asList(
            "lixir_guy",
            "Jackdedestroyer",
            "hhumanoid",
            "Jimbles_Joestar",
            "_crabcake",
            "Stallman1111",
            "_VectorV_",
            "Dev");
    //Cape textures
    private static final ResourceLocation BEEPER_CAPE = new ResourceLocation("vminus", "textures/cape/beeper_cape.png");
    private static final ResourceLocation GHOST_CAPE = new ResourceLocation("vminus", "textures/cape/ghost_cape.png");
    private static final ResourceLocation SHROUD_CAPE = new ResourceLocation("vminus", "textures/cape/shroud_cape.png");
    private static final ResourceLocation MARROW_CAPE = new ResourceLocation("vminus", "textures/cape/marrow_cape.png");
    private static final ResourceLocation PROTOTYPE_CAPE = new ResourceLocation("vminus", "textures/cape/prototype_cape.png");
    private static final ResourceLocation TROLL_CAPE = new ResourceLocation("vminus", "textures/cape/troll_cape.png");

    public static ResourceLocation getCapeTexture(AbstractClientPlayer player) {
        String capeId = player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(capability -> capability.cape_id).orElse("");
        if (ownsCape(player, capeId)) {
            switch(capeId) {
                case "beeper":
                    return BEEPER_CAPE;
                case "ghost":
                    return GHOST_CAPE;
                case "shroud":
                    return SHROUD_CAPE;
                case "marrow":
                    return MARROW_CAPE;
                case "prototype":
                    return PROTOTYPE_CAPE;
                case "troll":
                    return TROLL_CAPE;
            }
        }
        return null;
    }

    public static Boolean ownsCape(Entity entity, String capeId) {
        if (entity instanceof AbstractClientPlayer player) {
            String playerName = player.getGameProfile().getName();
            if ((PATREONS.contains(playerName) || BOOSTERS.contains(playerName)) && "beeper".equals(capeId)) {
                return true;
            } else if (BOOSTERS.contains(playerName) && "ghost".equals(capeId)) {
                return true;
            } else if (PATREONS.contains(playerName) && "marrow".equals(capeId)) {
                return true;
            } else if (PATREONS.contains(playerName) && "shroud".equals(capeId)) {
                return true;
            } else if (DEVELOPERS.contains(playerName) && "prototype".equals(capeId)) {
                return true;
            } else return CONTRIBUTORS.contains(playerName) && "troll".equals(capeId);
        }
        return false;
    }
}
