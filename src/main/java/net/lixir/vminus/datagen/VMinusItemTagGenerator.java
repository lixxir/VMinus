package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VMinusItemTagGenerator extends ItemTagsProvider {
    public VMinusItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, VMinus.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(VMinusTags.Items.BANNED);

        var woodenTools = tag(VMinusTags.Items.WOODEN_TOOLS);
        woodenTools.add(Items.WOODEN_SWORD);
        woodenTools.add(Items.WOODEN_PICKAXE);
        woodenTools.add(Items.WOODEN_AXE);
        woodenTools.add(Items.WOODEN_SHOVEL);
        woodenTools.add(Items.WOODEN_HOE);

        var stoneTools = tag(VMinusTags.Items.STONE_TOOLS);
        stoneTools.add(Items.STONE_SWORD);
        stoneTools.add(Items.STONE_PICKAXE);
        stoneTools.add(Items.STONE_AXE);
        stoneTools.add(Items.STONE_SHOVEL);
        stoneTools.add(Items.STONE_HOE);

        var goldenTools = tag(VMinusTags.Items.GOLDEN_TOOLS);
        goldenTools.add(Items.GOLDEN_SWORD);
        goldenTools.add(Items.GOLDEN_PICKAXE);
        goldenTools.add(Items.GOLDEN_AXE);
        goldenTools.add(Items.GOLDEN_SHOVEL);
        goldenTools.add(Items.GOLDEN_HOE);

        var ironTools = tag(VMinusTags.Items.IRON_TOOLS);
        ironTools.add(Items.IRON_SWORD);
        ironTools.add(Items.IRON_PICKAXE);
        ironTools.add(Items.IRON_AXE);
        ironTools.add(Items.IRON_SHOVEL);
        ironTools.add(Items.IRON_HOE);

        var diamondTools = tag(VMinusTags.Items.DIAMOND_TOOLS);
        diamondTools.add(Items.DIAMOND_SWORD);
        diamondTools.add(Items.DIAMOND_PICKAXE);
        diamondTools.add(Items.DIAMOND_AXE);
        diamondTools.add(Items.DIAMOND_SHOVEL);
        diamondTools.add(Items.DIAMOND_HOE);

        var netheriteTools = tag(VMinusTags.Items.NETHERITE_TOOLS);
        netheriteTools.add(Items.NETHERITE_SWORD);
        netheriteTools.add(Items.NETHERITE_PICKAXE);
        netheriteTools.add(Items.NETHERITE_AXE);
        netheriteTools.add(Items.NETHERITE_SHOVEL);
        netheriteTools.add(Items.NETHERITE_HOE);

        var leatherArmor = tag(VMinusTags.Items.LEATHER_ARMOR);
        leatherArmor.add(Items.LEATHER_HELMET);
        leatherArmor.add(Items.LEATHER_CHESTPLATE);
        leatherArmor.add(Items.LEATHER_LEGGINGS);
        leatherArmor.add(Items.LEATHER_BOOTS);

        var chainmailArmor = tag(VMinusTags.Items.CHAINMAIL_ARMOR);
        chainmailArmor.add(Items.CHAINMAIL_HELMET);
        chainmailArmor.add(Items.CHAINMAIL_CHESTPLATE);
        chainmailArmor.add(Items.CHAINMAIL_LEGGINGS);
        chainmailArmor.add(Items.CHAINMAIL_BOOTS);

        var goldenArmor = tag(VMinusTags.Items.GOLDEN_ARMOR);
        goldenArmor.add(Items.GOLDEN_HELMET);
        goldenArmor.add(Items.GOLDEN_CHESTPLATE);
        goldenArmor.add(Items.GOLDEN_LEGGINGS);
        goldenArmor.add(Items.GOLDEN_BOOTS);

        var ironArmor = tag(VMinusTags.Items.IRON_ARMOR);
        ironArmor.add(Items.IRON_HELMET);
        ironArmor.add(Items.IRON_CHESTPLATE);
        ironArmor.add(Items.IRON_LEGGINGS);
        ironArmor.add(Items.IRON_BOOTS);

        var diamondArmor = tag(VMinusTags.Items.DIAMOND_ARMOR);
        diamondArmor.add(Items.DIAMOND_HELMET);
        diamondArmor.add(Items.DIAMOND_CHESTPLATE);
        diamondArmor.add(Items.DIAMOND_LEGGINGS);
        diamondArmor.add(Items.DIAMOND_BOOTS);

        var netheriteArmor = tag(VMinusTags.Items.NETHERITE_ARMOR);
        netheriteArmor.add(Items.NETHERITE_HELMET);
        netheriteArmor.add(Items.NETHERITE_CHESTPLATE);
        netheriteArmor.add(Items.NETHERITE_LEGGINGS);
        netheriteArmor.add(Items.NETHERITE_BOOTS);

        var netheriteEquipment = tag(VMinusTags.Items.NETHERITE_EQUIPMENT);
        netheriteEquipment.addTag(VMinusTags.Items.NETHERITE_ARMOR);
        netheriteEquipment.addTag(VMinusTags.Items.NETHERITE_TOOLS);

        var goldenEquipment = tag(VMinusTags.Items.GOLDEN_EQUIPMENT);
        goldenEquipment.addTag(VMinusTags.Items.GOLDEN_ARMOR);
        goldenEquipment.addTag(VMinusTags.Items.GOLDEN_TOOLS);

        var ironEquipment = tag(VMinusTags.Items.IRON_EQUIPMENT);
        ironEquipment.addTag(VMinusTags.Items.IRON_ARMOR);
        ironEquipment.addTag(VMinusTags.Items.IRON_TOOLS);

        var diamondEquipment = tag(VMinusTags.Items.DIAMOND_EQUIPMENT);
        diamondEquipment.addTag(VMinusTags.Items.DIAMOND_ARMOR);
        diamondEquipment.addTag(VMinusTags.Items.DIAMOND_TOOLS);

        var bannerPatterns = tag(VMinusTags.Items.BANNER_PATTERNS);

        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            Item item = entry.getValue();
            if (item instanceof BannerPatternItem) {
                bannerPatterns.add(item);
            }
        }
    }
}
