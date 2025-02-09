package net.lixir.vminus.registry.util;

import net.lixir.vminus.VMinus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class VMinusTags {
    public static class Blocks {
        public static final TagKey<Block> SOUL_TORCHES = tag("soul_torches");
        public static final TagKey<Block> TORCHES = tag("torches");
        public static final TagKey<Block> FROGLIGHTS = tag("froglights");
        public static final TagKey<Block> MOB_HEADS = tag("mob_heads");
        public static final TagKey<Block> CONCRETE_POWDER = tag("concrete_powder");
        public static final TagKey<Block> BRUSHABLE_BLOCKS = tag("brushable_blocks");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(VMinus.ID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> IGNORES_TRANSLUCENCE = create(new ResourceLocation(VMinus.ID, "ignores_translucence"));

        private static TagKey<EntityType<?>> create(ResourceLocation p_203849_) {
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

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(VMinus.ID, name));
        }
    }
}
