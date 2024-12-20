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

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(VMinusMod.MODID, name));
        }
    }
}
