package net.lixir.vminus.cape;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class CapeAccounts {
    public static final UUID DEV_UUID = UUID.fromString("2bb68269-0733-442d-b746-6565060c1226");
    public static final Map<UUID, Flags> ACCOUNTS = ImmutableMap.<UUID, Flags>builder()

            // ----- DEV -----
            .put(uuid("2bb68269-0733-442d-b746-6565060c1226"), Flags.of().setAll()) // DEV
            .put(uuid("9b186dc0-30e9-4ba0-b068-acaf83d975aa"), Flags.of().setAll()) // lixir_dev
            .put(uuid("f7c07ce7-4ed1-43af-a839-64a1bd55d8bd"), Flags.of().setAll()) // hhumanoid
            .put(uuid("6fc4cabc-5f90-46df-86fb-2e35fdc99490"), Flags.of().setAll()) // Jackdedestroyer

            // ----- BOOSTERS -----
            .put(uuid("ff22b184-dabb-41bd-8dee-17d045d021b4"), Flags.of().setBooster()) // KRELO_X
            .put(uuid("83dfce82-3f69-4ae8-b661-4c2e6e0b7f6f"), Flags.of().setBooster()) // MILO
            .put(uuid("53f4a195-9b0b-403a-8229-4d1d0db60246"), Flags.of().setBooster()) // REINN
            .put(uuid("4dd1f189-a441-4a19-b183-83c4ad05b4f1"), Flags.of().setBooster()) // MEME_MAN
            .put(uuid("00a4bb78-a4d6-4a21-b1f4-781ffbba9300"), Flags.of().setBooster()) // STALMAN
            .put(uuid("8257d18d-6d3f-45b9-9e70-58a6cd35ea24"), Flags.of().setBooster()) // MOORE_GAMING
            .put(uuid("6192321a-94e6-4c8e-9925-6d1f8829974b"), Flags.of().setBooster()) // AZVALEN
            .put(uuid("782c820e-58f5-4390-9eb4-aaec5c165ccf"), Flags.of().setBooster()) // _AJGOR_
            .put(uuid("843c0ace-acc1-4d37-bca8-29d9b6c10dca"), Flags.of().setBooster()) // IDIOT_ERICH
            .put(uuid("80feac50-9b09-4076-9d21-cdfea015edd4"), Flags.of().setBooster()) // TUXXIC_202
            .put(uuid("0330aaaf-b69e-4036-8eaf-9e23e73861cb"), Flags.of().setBooster()) // EFFORT_A
            .put(uuid("482114bc-0b59-4a00-a9f9-137d3bf76260"), Flags.of().setBooster()) // MIMIKHUE
            .put(uuid("84dc12f6-0921-4da1-b87c-68aed30786b1"), Flags.of().setBooster()) // BONES418
            .put(uuid("c544c4ed-5332-489b-9135-e614e5dd4b32"), Flags.of().setBooster()) // ROIXEN
            .put(uuid("cd2d6090-fe6b-4ec4-ae25-2c782fe955ab"), Flags.of().setBooster()) // BABY_IRON_CEETH
            .put(uuid("84f045ce-9d4d-451a-8d43-1c7840e34eb0"), Flags.of().setBooster()) // ELECTRIC_GAME
            .put(uuid("7be244d3-e3f3-4205-bb7d-ad32c36b6f4e"), Flags.of().setBooster()) // MICROWAVEABLE_LAD
            .put(uuid("6941bbec-8ff0-4738-8d47-21df359ef8e3"), Flags.of().setBooster()) // OBSIDIAN_WRATH
            .put(uuid("c56744da-0ab8-447a-ba7b-b150b72457b2"), Flags.of().setBooster()) // WOOLEY_MOLEY
            .put(uuid("08842b55-8bd7-4023-bf32-e3d4e83b68c1"), Flags.of().setBooster()) // P1MEYS
            .put(uuid("ba749661-248a-400e-8b3c-88ad5ee4a95d"), Flags.of().setBooster()) // SUNSETTE_R
            .put(uuid("220d1dc1-bafd-4180-bb4f-1e9f12732e50"), Flags.of().setBooster()) // MINER_4_LIFE_REAL
            .put(uuid("a7c6375d-bd38-463d-a038-084ec65283a3"), Flags.of().setBooster()) // STELLAR_CROW
            .put(uuid("92b41f1a-d307-45ce-b1b2-1e5eac990295"), Flags.of().setBooster()) // PHSYCAL_MAN
            .put(uuid("034381ae-e0d2-4a31-b409-bfa4292a10ac"), Flags.of().setBooster()) // ZACHTOPLASM
            .put(uuid("e5f583c3-c3ea-4131-86b8-da100024c035"), Flags.of().setBooster()) // SIMEEOW

            // ----- PATRONS -----
            .put(uuid("b582223b-ff29-4277-ab55-0d3e9586163c"), Flags.of().setPatreon()) // SWEETYGAMER_2
            .put(uuid("54701376-b19a-4fc1-b107-74626b0d1bfb"), Flags.of().setPatreon()) // SLIME_SLABS
            .put(uuid("ff24d9e0-4e8e-44a9-ad90-f33810556532"), Flags.of().setPatreon()) // ULTRAMAN_001
            .put(uuid("93112bbd-f18e-478a-9c30-67682579fd22"), Flags.of().setPatreon()) // WISYCHU
            .put(uuid("55f2f1c7-8275-4605-8b95-b02a73a445fc"), Flags.of().setPatreon()) // MIMICROW
            .put(uuid("16bfd8ae-077e-43c2-a945-ec84ecc3a525"), Flags.of().setPatreon()) // _MAY_D_
            .put(uuid("03071095-a869-4aaf-8d9b-ba0cd2dd1374"), Flags.of().setPatreon()) // PIPSQUEAK_DA_GREAT
            .put(uuid("05bda48d-11e5-470f-8e0a-98ac9bd646ae"), Flags.of().setPatreon()) // SNAK3ARIES
            .put(uuid("bf99f166-7020-4f78-87c3-2de0ed3448a5"), Flags.of().setPatreon()) // EAT_BRAXTON
            .put(uuid("3f36f7e9-7459-43fe-87ce-4e8a5d47da80"), Flags.of().setPatreon()) // IZZY_BIZZY_45
            .put(uuid("899f8ce0-c7d0-4118-89e2-c45c6c2239cf"), Flags.of().setPatreon()) // BRIAN_IS_BRO
            .put(uuid("7809cf0d-23c1-47e9-a366-9b023cae1583"), Flags.of().setPatreon()) // ClephLeSDF
            .put(uuid("83593a7e-3049-47fb-b6da-717a20896ebf"), Flags.of().setPatreon()) // HoeNail
            .put(uuid("10679430-c1b4-4700-bd27-ca62742757fd"), Flags.of().setPatreon()) // rosymaplemoss
            .put(uuid("0308af15-2838-435d-ad27-3b134716f625"), Flags.of().setPatreon()) // Astolfo_oflotsA
            .put(uuid("5e48b02b-a601-4fc3-94e4-a3f88e18eb16"), Flags.of().setPatreon()) // Cap_keys
            .put(uuid("1ac9a975-e37d-4de7-b10e-e9be81b01454"), Flags.of().setPatreon()) // spoiled_rotten
            .put(uuid("a3ad64d1-39d6-45e4-b42f-7fba220dfe67"), Flags.of().setPatreon()) // I_Orion
            .put(uuid("24375dec-5a2b-4255-9f40-042181f3f60b"), Flags.of().setPatreon()) // Darklord6323
            .put(uuid("2f015e04-5d94-4661-a5a4-6f8f6af755de"), Flags.of().setPatreon()) // Tealotl
            .put(uuid("53e58cfb-d3db-4c0c-8d5e-c104f0139e86"), Flags.of().setPatreon()) // Camriod_Core
            .put(uuid("31b718d8-f76a-4868-a56c-b79a363ff0a9"), Flags.of().setPatreon()) // NuclearDiamond
            .put(uuid("c6e73dcd-ace9-4c13-99a1-76ab218a72ad"), Flags.of().setPatreon()) // SharkyTheNarwhal
            .put(uuid("6ccc5244-e72e-4515-8482-1b4116bd2b85"), Flags.of().setPatreon()) // El_Molgajete
            .put(uuid("3f06c48a-8141-413a-8cc7-e123da7709a3"), Flags.of().setPatreon()) // DreLmao
            .put(uuid("a7ce848e-4e4d-4878-9abc-129636cc87a0"), Flags.of().setPatreon()) // Alphei
            .put(uuid("74a16b25-1652-4cf8-8eb8-4a4a25008e1a"), Flags.of().setPatreon()) // Shonestain
            .put(uuid("a2d81e8c-e363-48dc-af5b-24b0e57a9472"), Flags.of().setPatreon()) // Oddchilly
            .put(uuid("1e5a53a1-cf0f-4138-bcdd-5dfaa217dcae"), Flags.of().setPatreon()) // CaptainRageJoin
            .put(uuid("09f24630-47b0-40a2-b083-99552aa9cbb3"), Flags.of().setPatreon()) // Dr_pimpf
            .put(uuid("a078db8a-2dd1-4671-b50b-fee0845c82cf"), Flags.of().setPatreon()) // 8nxtsuke
            .put(uuid("d94fe54f-53a7-45f8-b399-09b7523445e1"), Flags.of().setPatreon()) // hudsonjr
            .put(uuid("cf6cdaba-1b84-42cc-ac78-24b1569abd3f"), Flags.of().setPatreon()) // Icy_Craby
            .put(uuid("0f0b884b-0cd2-4acf-b885-d567992eb13d"), Flags.of().setPatreon()) // vveniamin
            .put(uuid("662d6b87-79b8-40af-8560-fcedf851b233"), Flags.of().setPatreon()) // Stupid_Gator
            .put(uuid("68a3937b-818f-4fd9-8823-78bc1b2c1d0f"), Flags.of().setPatreon()) // connorlicious
            .put(uuid("bf3914e6-ce74-4028-87c8-762daa3547b2"), Flags.of().setPatreon()) // MichaelUllman
            .put(uuid("7ec07338-63eb-4703-a161-6730a6a6f16d"), Flags.of().setPatreon()) // TropicalPoptart
            .put(uuid("e1ab672a-8466-4b76-bd16-64b84ea73dc8"), Flags.of().setPatreon()) // Davirone
            .put(uuid("53dbfea3-386e-48cf-b57c-68e83dd0e5e5"), Flags.of().setPatreon()) // Mweems
            .put(uuid("9b2df4c2-d603-495e-8bcb-1d82ac851358"), Flags.of().setPatreon()) // The_Scarlet_Lamb
            .put(uuid("06f34556-172e-4549-9ae2-b33f9614c006"), Flags.of().setPatreon()) // Jet1888
            .put(uuid("b106f653-fad1-41c2-a5c8-622fadd3edeb"), Flags.of().setPatreon()) // LFMoth
            .put(uuid("197e891c-bf82-4e6b-8137-4bcd11d4dfa9"), Flags.of().setPatreon()) // Bizuter201
            .put(uuid("bf515b70-104c-4135-82c7-e30877cc9006"), Flags.of().setPatreon()) // friedfische
            .put(uuid("6fe111d7-7037-4acb-bb53-c9ecb5a7743b"), Flags.of().setPatreon()) // OcenEverence
            .put(uuid("619376ed-867c-487a-bc3f-389069d8a53e"), Flags.of().setPatreon()) // MrSyntherman64

            // ----- COMBINED -----
            .put(uuid("b9b106ae-7c16-4426-950f-ea48ca7acc0b"), Flags.of().setBooster().setContributor()) // _VECTOR_V_
            .put(uuid("678a2dd1-a05d-4093-9b71-cbf9e322eabd"), Flags.of().setBooster().setPatreon()) // Booneraniaro
            .put(uuid("0120b5e8-fd76-41bd-b06d-b1575e9da492"), Flags.of().setBooster().setPatreon()) // Simonisnear
            .put(uuid("8ad9d98b-d084-4d50-a0a5-26192b0ba10f"), Flags.of().setBooster().setPatreon()) // TheScarletRot
            .put(uuid("be8e6bf6-421d-49c6-8ed8-5215a46ff88c"), Flags.of().setBooster().setPatreon()) // DARIUS_ZE_BAGUETTE
            .put(uuid("2954a1e9-6593-4574-8088-c71793c121a9"), Flags.of().setBooster().setPatreon()) // FudgiecatDX
            .put(uuid("dab6c9d4-fa37-49ef-8ac6-0f0b7e563256"), Flags.of().setBooster().setPatreon()) // Lobotomies

            .build();

    public static @Nullable Flags fromUUID(@NotNull UUID uuid) {
        return ACCOUNTS.get(uuid);
    }

    private static UUID uuid(String id) {
        return UUID.fromString(id);
    }

    public static class Flags {
        private boolean patreon = false;
        private boolean developer = false;
        private boolean booster = false;
        private boolean contributor = false;

        private Flags() {}

        @Contract(value = " -> new", pure = true)
        public static @NotNull Flags of() {
            return new Flags();
        }

        public Flags setAll() {
            patreon = true;
            developer = true;
            booster = true;
            contributor = true;
            return this;
        }

        public Flags setPatreon() {
            patreon = true;
            return this;
        }

        public Flags setContributor() {
            contributor = true;
            return this;
        }

        public Flags setBooster() {
            booster = true;
            return this;
        }

        public Flags developer() {
            developer = true;
            return this;
        }

        public boolean isPatreon() {
            return patreon;
        }

        public boolean isDeveloper() {
            return developer;
        }

        public boolean isBooster() {
            return booster;
        }

        public boolean isContributor() {
            return contributor;
        }
    }
}
