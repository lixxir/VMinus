package net.lixir.vminus.registry.util;

import net.lixir.vminus.VMinusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class VMinusTags {
    public static class Blocks {
        public static final TagKey<Block> SOUL_TORCHES = tag("soul_torches");
        public static final TagKey<Block> TORCHES = tag("torches");
        public static final TagKey<Block> FROGLIGHTS = tag("soul_torches");
        public static final TagKey<Block> MOB_HEADS = tag("mob_heads");
        public static final TagKey<Block> CONCRETE_POWDER = tag("concrete_powder");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(VMinusMod.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GOLDEN_TOOLS = tag("golden_tools");
        public static final TagKey<Item> GOLDEN_ARMOR = tag("golden_armor");
        public static final TagKey<Item> GOLDEN_EQUIPMENT = tag("golden_equipment");
        public static final TagKey<Item> LIGHTFOOTED = tag("lightfooted");
        public static final TagKey<Item> BANNED = tag("banned");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(VMinusMod.MODID, name));
        }
    }
}
