package net.lixir.vminus;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.List;

public class CapeHelper {
    //Name lists
    private static final List<String> patreons = Arrays.asList("lixir_guy", "Sweetygamer2", "Dev", "SlimeSlabs", "Ultraman001", "Booneraniaro", "rosymaplemoss", "HoeNail", "ClephLeSDF", "BrianIsBro", "IzzyBizzy45");
    private static final List<String> boosters = Arrays.asList("lixir_guy", "Jackdedestroyer", "KreloX", "Dev", "_Ajgor_", "Azvalen", "Goongamer77", "MooreGaming1324", "Meme___Man", "reindawn", "Simonisnear", "miloq__", "Stallman1111");
    private static final List<String> developers = Arrays.asList("lixir_guy", "Jackdedestroyer", "hhumanoid", "Dev");
    private static final List<String> contributors = Arrays.asList("lixir_guy", "Jackdedestroyer", "hhumanoid", "Jimbles_Joestar", "_crabcake", "Stallman1111", "_VectorV_", "Dev");
    //Cape textures
    private static final ResourceLocation beeperCape = new ResourceLocation("vminus", "textures/cape/beeper_cape.png");
    private static final ResourceLocation ghostCape = new ResourceLocation("vminus", "textures/cape/ghost_cape.png");
    private static final ResourceLocation shroudCape = new ResourceLocation("vminus", "textures/cape/shroud_cape.png");
    private static final ResourceLocation marrowCape = new ResourceLocation("vminus", "textures/cape/marrow_cape.png");
    private static final ResourceLocation prototypeCape = new ResourceLocation("vminus", "textures/cape/prototype_cape.png");
    private static final ResourceLocation trollCape = new ResourceLocation("vminus", "textures/cape/troll_cape.png");

    public static ResourceLocation getCapeTexture(AbstractClientPlayer player) {
        String capeId = player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(capability -> capability.cape_id).orElse("");
        if (ownsCape(player, capeId)) {
            if ("beeper".equals(capeId)) {
                return beeperCape;
            } else if ("ghost".equals(capeId)) {
                return ghostCape;
            } else if ("shroud".equals(capeId)) {
                return shroudCape;
            } else if ("marrow".equals(capeId)) {
                return marrowCape;
            } else if ("prototype".equals(capeId)) {
                return prototypeCape;
            } else if ("troll".equals(capeId)) {
                return trollCape;
            }
        }
        return null;
    }

    public static Boolean ownsCape(Entity entity, String capeId) {
        if (entity instanceof AbstractClientPlayer player) {
            String playerName = player.getGameProfile().getName();
            if ((patreons.contains(playerName) || boosters.contains(playerName)) && "beeper".equals(capeId)) {
                return true;
            } else if (boosters.contains(playerName) && "ghost".equals(capeId)) {
                return true;
            } else if (patreons.contains(playerName) && "marrow".equals(capeId)) {
                return true;
            } else if (patreons.contains(playerName) && "shroud".equals(capeId)) {
                return true;
            } else if (developers.contains(playerName) && "prototype".equals(capeId)) {
                return true;
            } else return contributors.contains(playerName) && "troll".equals(capeId);
        }
        return false;
    }
}
