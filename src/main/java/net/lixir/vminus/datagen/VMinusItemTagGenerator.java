package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class VMinusItemTagGenerator extends ItemTagsProvider {
    public VMinusItemTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper, BlockTagsProvider blockTagsProvider) {
        super(generator, blockTagsProvider, VMinusMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Item> banned = tag(VMinusTags.Items.BANNED);
        TagAppender<Item> golden_tools = tag(VMinusTags.Items.GOLDEN_TOOLS);

        golden_tools.add(
                Items.GOLDEN_SWORD,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_AXE,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_HOE
        );

        TagAppender<Item> golden_armor = tag(VMinusTags.Items.GOLDEN_ARMOR);
        golden_armor.add(
                Items.GOLDEN_HELMET,
                Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_LEGGINGS,
                Items.GOLDEN_BOOTS
        );



    }
}
