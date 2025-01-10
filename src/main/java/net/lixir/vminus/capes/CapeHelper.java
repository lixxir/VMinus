package net.lixir.vminus.capes;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CapeHelper {

    private static final String DEV_NAME = "Dev";

    private static final List<CapeOwner> PATRONS = Arrays.asList(
            CapeOwner.LIXIR,
            CapeOwner.SWEETY,
            CapeOwner.SLIME_SLABS,
            CapeOwner.ELATIUM_NETWORK,
            CapeOwner.BOONERANIARO,
            CapeOwner.ROSY_MAPLE_MOSS,
            CapeOwner.HOE_NAIL,
            CapeOwner.CLEPH_LE_SDF,
            CapeOwner.BRIAN_IS_BRO,
            CapeOwner.IZZY_BIZZY,
            CapeOwner.SIMON_IS_NEAR,
            CapeOwner.MIASMA,
            CapeOwner.UURETCH,
            CapeOwner.ASTOLOFO_OFLOTSA,
            CapeOwner.DARIUSZEBAGUETTE,
            CapeOwner.CAP_KEYS,
            CapeOwner.WISYCHU,
            CapeOwner.MIMICROW,
            CapeOwner.MAYD,
            CapeOwner.PIPSQUEAKDAGREAT,
            CapeOwner.SNAK3ARIES,
            CapeOwner.EAT_BRAXTON,
            CapeOwner.SPOILED_ROTTEN,
            CapeOwner.L_ORION,
            CapeOwner.DARKLORD6323,
            CapeOwner.TEALOTL,
            CapeOwner.CAMRIOD_CORE,
            CapeOwner.NUCLEARDIAMOND,
            CapeOwner.SHARKYTHENARWHAL,
            CapeOwner.SHONESTAIN
    );

    private static final List<CapeOwner> BOOSTERS = Arrays.asList(
            CapeOwner.LIXIR,
            CapeOwner.JACKDEDESTROYER,
            CapeOwner.KRELOX,
            CapeOwner._AJGOR_,
            CapeOwner.AZVALEN,
            CapeOwner.MOOREGAMING1324,
            CapeOwner.MEME___MAN,
            CapeOwner.REINDAWN,
            CapeOwner.SIMON_IS_NEAR,
            CapeOwner.MILOQ__,
            CapeOwner.STALLMAN1111,
            CapeOwner.DARIUSZEBAGUETTE,
            CapeOwner.IDIOTERICH,
            CapeOwner.TUXXIC202,
            CapeOwner.EFFORT_A,
            CapeOwner.UURETCH,
            CapeOwner.VECTORV,
            CapeOwner.BONES418
    );

    private static final List<CapeOwner> DEVELOPERS = Arrays.asList(
            CapeOwner.LIXIR,
            CapeOwner.JACKDEDESTROYER,
            CapeOwner.HHUMANOID
    );

    private static final List<CapeOwner> CONTRIBUTORS = Arrays.asList(
            CapeOwner.LIXIR,
            CapeOwner.JACKDEDESTROYER,
            CapeOwner.HHUMANOID,
            CapeOwner.JIMBLES_JOESTAR,
            CapeOwner.CRABCAKE,
            CapeOwner.STALLMAN1111,
            CapeOwner.VECTORV,
            CapeOwner.PONDEROSA
    );

    private static final List<CapeOwner> PHOTON_BUILDER = Arrays.asList(
            CapeOwner.BOONERANIARO,
            CapeOwner.LIXIR,
            CapeOwner.JACKDEDESTROYER,
            CapeOwner.VECTORV,
            CapeOwner.HHUMANOID,
            CapeOwner.LEXALIS8,
            CapeOwner.LUCENTIC
    );




    public static ResourceLocation getCapeTexture(AbstractClientPlayer player) {
        String capeId = player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .map(capability -> capability.cape_id).orElse("");
        if (ownsCape(player, capeId)) {
            Cape cape = Cape.fromId(capeId);
            if (cape != null) {
                return cape.getTexture();
            }
        }
        return null;
    }

    public static List<Cape> getAvailableCapes(Player player) {
        List<Cape> availableCapes = new ArrayList<>();
        for (Cape cape : Cape.values()) {
            if (ownsCape(player, cape.getId())) {
                availableCapes.add(cape);
            }
        }
        return availableCapes;
    }

    public static Boolean ownsCape(Entity entity, String capeId) {
        if (entity instanceof Player player) {
            UUID playerUUID = player.getGameProfile().getId();
            String playerName = player.getGameProfile().getName();



            switch (capeId) {
                case "beeper" -> {
                    if (matchesUuid(playerUUID, PATRONS) || matchesUuid(playerUUID, BOOSTERS) || DEV_NAME.equals(playerName))
                        return true;
                }
                case "ghost" -> {
                    if (matchesUuid(playerUUID, BOOSTERS) || DEV_NAME.equals(playerName))
                        return true;
                }
                case "marrow", "shroud" -> {
                    if (matchesUuid(playerUUID, PATRONS) || DEV_NAME.equals(playerName))
                        return true;
                }
                case "prototype" -> {
                    if (matchesUuid(playerUUID, DEVELOPERS) || DEV_NAME.equals(playerName))
                        return true;
                }
                case "photon" -> {
                    if (matchesUuid(playerUUID, PHOTON_BUILDER) || DEV_NAME.equals(playerName))
                        return true;
                }
                case "troll" -> {
                    if (matchesUuid(playerUUID, CONTRIBUTORS) || DEV_NAME.equals(playerName))
                        return true;
                }
            }
        }
        return false;
    }


    private static boolean matchesUuid(UUID playerUUID, List<CapeOwner> owners) {
        for (CapeOwner owner : owners) {
            if (owner.getUuid().equals(playerUUID)) {
                return true;
            }
        }
        return false;
    }
}
