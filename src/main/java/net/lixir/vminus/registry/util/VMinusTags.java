package net.lixir.vminus.registry.util;

import net.lixir.vminus.VMinus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class VMinusTags {
    public static class Blocks {
        public static final TagKey<Block> WOODEN_FENCE_GATES = tag("soul_torches");
        public static final TagKey<Block> REDSTONE_TORCHES = tag("redstone_torches");
        public static final TagKey<Block> SOUL_TORCHES = tag("soul_torches");
        public static final TagKey<Block> TORCHES = tag("torches");
        public static final TagKey<Block> ALL_TORCHES = tag("all_torches");
        public static final TagKey<Block> FROGLIGHTS = tag("froglights");
        public static final TagKey<Block> MOB_HEADS = tag("mob_heads");
        public static final TagKey<Block> CONCRETE_POWDER = tag("concrete_powder");
        public static final TagKey<Block> BRUSHABLE = tag("brushable");
        public static final TagKey<Block> CAN_SUSTAIN_PLANTS = tag("can_sustain_plants");
        public static final TagKey<Block> CAN_SUSTAIN_CACTUS = tag("can_sustain_cactus");
        public static final TagKey<Block> CAN_SUSTAIN_DEAD_BUSH = tag("can_sustain_dead_bush");
        public static final TagKey<Block> DYEABLE = tag("dyeable");
        public static final TagKey<Block> DYED = tag("dyed");
        public static final TagKey<Block> DYED_WOOL = tag("dyed/wool");
        public static final TagKey<Block> DYED_TERRACOTTA = tag("dyed/terracotta");
        public static final TagKey<Block> DYED_CONCRETE = tag("dyed/concrete");
        public static final TagKey<Block> DYED_BEDS = tag("dyed/beds");
        public static final TagKey<Block> DYED_BANNERS = tag("dyed/banners");
        public static final TagKey<Block> DYED_CANDLES = tag("dyed/candles");
        public static final TagKey<Block> DYED_GLAZED_TERRACOTTA = tag("dyed/glazed_terracotta");
        public static final TagKey<Block> DYED_CARPETS = tag("dyed/carpets");
        public static final TagKey<Block> DYED_CONCRETE_POWDER = tag("dyed/concrete_powder");
        public static final TagKey<Block> DYED_STAINED_GLASS = tag("dyed/stained_glass");
        public static final TagKey<Block> DYED_STAINED_GLASS_PANE = tag("dyed/stained_glass_pane");
        public static final TagKey<Block> GRASSES = tag("grasses");
        public static final TagKey<Block> TALL_GRASSES = tag("tall_grasses");
        public static final TagKey<Block> LEASHABLE = tag("leashable");
        public static final TagKey<Block> COCAO_PLANTABLE_ON = tag("cocao_plantable_on");
        public static final TagKey<Block> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
        public static final TagKey<Block> DEAD_CORALS = tag("dead_corals");
        public static final TagKey<Block> DEAD_CORAL_FANS = tag("dead_coral_fans");

        private static @NotNull TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(VMinus.ID, name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> BLUNT_DAMAGE = create(new ResourceLocation(VMinus.ID, "protection/blunt"));
        public static final TagKey<DamageType> BLAST_DAMAGE = create(new ResourceLocation(VMinus.ID, "protection/blast"));
        public static final TagKey<DamageType> FALL_DAMAGE = create(new ResourceLocation(VMinus.ID, "protection/fall"));
        public static final TagKey<DamageType> FIRE_DAMAGE = create(new ResourceLocation(VMinus.ID, "protection/fire"));
        public static final TagKey<DamageType> MAGIC_DAMAGE = create(new ResourceLocation(VMinus.ID, "protection/magic"));

        private static @NotNull TagKey<DamageType> create(ResourceLocation p_203849_) {
            return TagKey.create(Registries.DAMAGE_TYPE, p_203849_);
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> OOZE = create("ooze");

        private static @NotNull TagKey<Fluid> create(String id) {
            return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(VMinus.ID, id));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> ZOMBIES = create(new ResourceLocation(VMinus.ID, "zombies"));
        public static final TagKey<EntityType<?>> BANNED = create(new ResourceLocation(VMinus.ID, "banned"));

        private static @NotNull TagKey<EntityType<?>> create(ResourceLocation p_203849_) {
            return TagKey.create(Registries.ENTITY_TYPE, p_203849_);
        }
    }

    public static class Items {
        public static final TagKey<Item> BANNED = tag("banned");

        public static final TagKey<Item> WOODEN_TOOLS = tag("tools/wooden");
        public static final TagKey<Item> STONE_TOOLS = tag("tools/stone");
        public static final TagKey<Item> GOLDEN_TOOLS = tag("tools/golden");
        public static final TagKey<Item> IRON_TOOLS = tag("tools/iron");
        public static final TagKey<Item> DIAMOND_TOOLS = tag("tools/diamond");
        public static final TagKey<Item> NETHERITE_TOOLS = tag("tools/netherite");

        public static final TagKey<Item> LEATHER_ARMOR = tag("armor/leather");
        public static final TagKey<Item> CHAINMAIL_ARMOR = tag("armor/chainmail");
        public static final TagKey<Item> GOLDEN_ARMOR = tag("armor/golden");
        public static final TagKey<Item> IRON_ARMOR = tag("armor/iron");
        public static final TagKey<Item> DIAMOND_ARMOR = tag("armor/diamond");
        public static final TagKey<Item> NETHERITE_ARMOR = tag("armor/netherite");
        public static final TagKey<Item> ARMOR = tag("armor");

        public static final TagKey<Item> GOLDEN_EQUIPMENT = tag("equipment/golden");
        public static final TagKey<Item> IRON_EQUIPMENT = tag("equipment/iron");
        public static final TagKey<Item> DIAMOND_EQUIPMENT = tag("equipment/diamond");
        public static final TagKey<Item> NETHERITE_EQUIPMENT = tag("equipment/netherite");

        public static final TagKey<Item> UNCOMMON = tag("rarity/uncommon");
        public static final TagKey<Item> RARE = tag("rarity/rare");
        public static final TagKey<Item> EPIC = tag("rarity/epic");
        public static final TagKey<Item> LEGENDARY = tag("rarity/legendary");
        public static final TagKey<Item> INVERTED = tag("rarity/inverted");
        public static final TagKey<Item> UNOBTAINABLE = tag("rarity/unobtainable");
        public static final TagKey<Item> DELICACY = tag("rarity/delicacy");

        public static final TagKey<Item> BANNER_PATTERNS = tag("banner_patterns");
        public static final TagKey<Item> CONCRETE_POWDER = tag("concrete_powder");
        public static final TagKey<Item> GLAZED_TERRACOTTA = tag("glazed_terracotta");
        public static final TagKey<Item> CHEST_BOATS = tag("chest_boats");
        public static final TagKey<Item> BOATS = tag("boats");
        public static final TagKey<Item> SHULKER_BOXES = tag("shulker_boxes");
        public static final TagKey<Item> BUCKETS = tag("buckets");
        public static final TagKey<Item> LIQUID_BUCKETS = tag("buckets/liquid");
        public static final TagKey<Item> SOLID_BUCKETS = tag("buckets/solid");
        public static final TagKey<Item> MOB_BUCKETS = tag("buckets/mob");
        public static final TagKey<Item> FOOD_BUCKETS = tag("buckets/food");
        public static final TagKey<Item> DEAD_CORAL_BLOCKS = tag("dead_coral_blocks");
        public static final TagKey<Item> DEAD_CORALS = tag("dead_corals");
        public static final TagKey<Item> DEAD_CORAL_FANS = tag("dead_coral_fans");
        public static final TagKey<Item> WOODEN_FENCE_GATES = tag("wooden_fence_gates");

        private static @NotNull TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(VMinus.ID, name));
        }
    }
}
