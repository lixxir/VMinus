package net.lixir.vminus.helpers;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CapeHelper {
    // UUIDs
    private static final UUID LIXIR = UUID.fromString("16749ceb-714d-40c2-a228-d48a0079cdc0");
    private static final UUID SWEETY = UUID.fromString("b582223b-ff29-4277-ab55-0d3e9586163c");
    private static final UUID SLIME_SLABS = UUID.fromString("54701376-b19a-4fc1-b107-74626b0d1bfb");
    private static final UUID ELATIUM_NETWORK = UUID.fromString("ff24d9e0-4e8e-44a9-ad90-f33810556532");
    private static final UUID BOONERANIARO = UUID.fromString("678a2dd1-a05d-4093-9b71-cbf9e322eabd");
    private static final UUID ROSY_MAPLE_MOSS = UUID.fromString("10679430-c1b4-4700-bd27-ca62742757fd");
    private static final UUID HOE_NAIL = UUID.fromString("83593a7e-3049-47fb-b6da-717a20896ebf");
    private static final UUID CLEPH_LE_SDF = UUID.fromString("7809cf0d-23c1-47e9-a366-9b023cae1583");
    private static final UUID BRIAN_IS_BRO = UUID.fromString("899f8ce0-c7d0-4118-89e2-c45c6c2239cf");
    private static final UUID IZZY_BIZZY = UUID.fromString("3f36f7e9-7459-43fe-87ce-4e8a5d47da80");
    private static final UUID SIMON_IS_NEAR = UUID.fromString("0120b5e8-fd76-41bd-b06d-b1575e9da492");
    private static final UUID MIASMA = UUID.fromString("8ad9d98b-d084-4d50-a0a5-26192b0ba10f");
    private static final UUID UURETCH = UUID.fromString("22a2c044-9ee5-41a8-8523-51a9f77d84de");
    private static final UUID ASTOLOFO_OFLOTSA = UUID.fromString("22a2c044-9ee5-41a8-8523-51a9f77d84de");
    private static final UUID DARIUSZEBAGUETTE = UUID.fromString("be8e6bf6-421d-49c6-8ed8-5215a46ff88c");
    private static final UUID CAP_KEYS = UUID.fromString("5e48b02b-a601-4fc3-94e4-a3f88e18eb16");
    private static final UUID JACKDEDESTROYER = UUID.fromString("6fc4cabc-5f90-46df-86fb-2e35fdc99490");
    private static final UUID HHUMANOID = UUID.fromString("f7c07ce7-4ed1-43af-a839-64a1bd55d8bd");
    private static final UUID JIMBLES_JOESTAR = UUID.fromString("3e71ca01-00bf-4965-b31c-b9b7de968447");
    private static final UUID VECTORV = UUID.fromString("b9b106ae-7c16-4426-950f-ea48ca7acc0b");
    private static final UUID KRELOX = UUID.fromString("ff22b184-dabb-41bd-8dee-17d045d021b4");
    private static final UUID PONDEROSA = UUID.fromString("3e49c6dd-b06b-4110-bd27-d7d18bc52b79");
    private static final UUID STALLMAN1111 = UUID.fromString("00a4bb78-a4d6-4a21-b1f4-781ffbba9300");
    private static final UUID CRABCAKE = UUID.fromString("92252782-2ebd-4bce-989e-d8e1729b7c31");
    private static final UUID WISYCHU = UUID.fromString("93112bbd-f18e-478a-9c30-67682579fd22");
    private static final UUID MIMICROW = UUID.fromString("55f2f1c7-8275-4605-8b95-b02a73a445fc");
    private static final UUID MAYD = UUID.fromString("16bfd8ae-077e-43c2-a945-ec84ecc3a525");
    private static final UUID PIPSQUEAKDAGREAT = UUID.fromString("03071095-a869-4aaf-8d9b-ba0cd2dd1374");
    private static final UUID SNAK3ARIES = UUID.fromString("05bda48d-11e5-470f-8e0a-98ac9bd646ae");
    private static final UUID EAT_BRAXTON = UUID.fromString("bf99f166-7020-4f78-87c3-2de0ed3448a5");
    private static final UUID SPOILED_ROTTEN = UUID.fromString("1ac9a975-e37d-4de7-b10e-e9be81b01454");
    private static final UUID L_ORION = UUID.fromString("a3ad64d1-39d6-45e4-b42f-7fba220dfe67");
    private static final UUID DARKLORD6323 = UUID.fromString("24375dec-5a2b-4255-9f40-042181f3f60b");
    private static final UUID TEALOTL = UUID.fromString("2f015e04-5d94-4661-a5a4-6f8f6af755de");
    private static final UUID _AJGOR_ = UUID.fromString("782c820e-58f5-4390-9eb4-aaec5c165ccf");
    private static final UUID AZVALEN = UUID.fromString("6192321a-94e6-4c8e-9925-6d1f8829974b");
    private static final UUID MOOREGAMING1324 = UUID.fromString("8257d18d-6d3f-45b9-9e70-58a6cd35ea24");
    private static final UUID MEME___MAN = UUID.fromString("4dd1f189-a441-4a19-b183-83c4ad05b4f1");
    private static final UUID REINDAWN = UUID.fromString("53f4a195-9b0b-403a-8229-4d1d0db60246");
    private static final UUID MILOQ__ = UUID.fromString("83dfce82-3f69-4ae8-b661-4c2e6e0b7f6f");
    private static final UUID IDIOTERICH = UUID.fromString("843c0ace-acc1-4d37-bca8-29d9b6c10dca");
    private static final UUID TUXXIC202 = UUID.fromString("80feac50-9b09-4076-9d21-cdfea015edd4");
    private static final UUID EFFORT_A = UUID.fromString("0330aaaf-b69e-4036-8eaf-9e23e73861cb");
    private static final UUID CAMRIOD_CORE = UUID.fromString("53e58cfb-d3db-4c0c-8d5e-c104f0139e86");
    private static final UUID NUCLEARDIAMOND = UUID.fromString("31b718d8-f76a-4868-a56c-b79a363ff0a9");
    private static final UUID SHARKYTHENARWHAL = UUID.fromString("c6e73dcd-ace9-4c13-99a1-76ab218a72ad");
    //Name lists
    private static final List<UUID> PATREONS = Arrays.asList(
            LIXIR,
            SWEETY,
            SLIME_SLABS,
            ELATIUM_NETWORK,
            BOONERANIARO,
            ROSY_MAPLE_MOSS,
            HOE_NAIL,
            CLEPH_LE_SDF,
            BRIAN_IS_BRO,
            IZZY_BIZZY,
            SIMON_IS_NEAR,
            MIASMA,
            UURETCH,
            ASTOLOFO_OFLOTSA,
            DARIUSZEBAGUETTE,
            CAP_KEYS,
            WISYCHU,
            MIMICROW,
            MAYD,
            PIPSQUEAKDAGREAT,
            SNAK3ARIES,
            EAT_BRAXTON,
            SPOILED_ROTTEN,
            L_ORION,
            DARKLORD6323,
            TEALOTL,
            CAMRIOD_CORE,
            NUCLEARDIAMOND,
            SHARKYTHENARWHAL);
    private static final List<UUID> BOOSTERS = Arrays.asList(
            LIXIR,
            JACKDEDESTROYER,
            KRELOX,
            _AJGOR_,
            AZVALEN,
            MOOREGAMING1324,
            MEME___MAN,
            REINDAWN,
            SIMON_IS_NEAR,
            MILOQ__,
            STALLMAN1111,
            DARIUSZEBAGUETTE,
            IDIOTERICH,
            TUXXIC202,
            EFFORT_A,
            UURETCH);
    private static final List<UUID> DEVELOPERS = Arrays.asList(
            LIXIR,
            JACKDEDESTROYER,
            HHUMANOID);
    private static final List<UUID> CONTRIBUTORS = Arrays.asList(
            LIXIR,
            JACKDEDESTROYER,
            HHUMANOID,
            JIMBLES_JOESTAR,
            CRABCAKE,
            STALLMAN1111,
            VECTORV,
            PONDEROSA);
    //Cape texture
    private static final ResourceLocation BEEPER_CAPE = new ResourceLocation("vminus", "textures/cape/beeper_cape.png");
    private static final ResourceLocation GHOST_CAPE = new ResourceLocation("vminus", "textures/cape/ghost_cape.png");
    private static final ResourceLocation SHROUD_CAPE = new ResourceLocation("vminus", "textures/cape/shroud_cape.png");
    private static final ResourceLocation MARROW_CAPE = new ResourceLocation("vminus", "textures/cape/marrow_cape.png");
    private static final ResourceLocation PROTOTYPE_CAPE = new ResourceLocation("vminus", "textures/cape/prototype_cape.png");
    private static final ResourceLocation TROLL_CAPE = new ResourceLocation("vminus", "textures/cape/troll_cape.png");

    public static ResourceLocation getCapeTexture(AbstractClientPlayer player) {
        String capeId = player.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).map(capability -> capability.cape_id).orElse("");
        if (ownsCape(player, capeId)) {
            switch (capeId) {
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
            UUID playerUUID = player.getGameProfile().getId();
            String playerName = player.getGameProfile().getName();
            if ((PATREONS.contains(playerUUID) || BOOSTERS.contains(playerUUID) || playerName.equals("Dev")) && "beeper".equals(capeId)) {
                return true;
            } else if ((BOOSTERS.contains(playerUUID) || playerName.equals("Dev")) && "ghost".equals(capeId)) {
                return true;
            } else if ((PATREONS.contains(playerUUID) || playerName.equals("Dev")) && "marrow".equals(capeId)) {
                return true;
            } else if ((PATREONS.contains(playerUUID) || playerName.equals("Dev")) && "shroud".equals(capeId)) {
                return true;
            } else if ((DEVELOPERS.contains(playerUUID) || playerName.equals("Dev")) && "prototype".equals(capeId)) {
                return true;
            } else return (CONTRIBUTORS.contains(playerUUID) || playerName.equals("Dev")) && "troll".equals(capeId);
        }
        return false;
    }
}
