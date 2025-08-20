package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.datagen.util.tag.VItemTagGenerator;
import net.lixir.vminus.mixins.items.BoatItemAccessor;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VMinusItemTagGenerator extends VItemTagGenerator {
    public VMinusItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                                  CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, existingFileHelper, VMinus.ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        super.addTags(pProvider);
        tag(VMinusTags.Items.CREATIVE_ONLY)
                .add(Items.COMMAND_BLOCK)
                .add(Items.COMMAND_BLOCK_MINECART)
                .add(Items.CHAIN_COMMAND_BLOCK)
                .add(Items.JIGSAW)
                .add(Items.BARRIER)
                .add(Items.DEBUG_STICK)
                .add(Items.LIGHT)
                .add(Items.STRUCTURE_VOID)
                .add(Items.STRUCTURE_BLOCK);
        tag(VMinusTags.Items.DEAD_CORAL_BLOCKS)
                .add(Items.DEAD_TUBE_CORAL_BLOCK)
                .add(Items.DEAD_BRAIN_CORAL_BLOCK)
                .add(Items.DEAD_BUBBLE_CORAL_BLOCK)
                .add(Items.DEAD_FIRE_CORAL_BLOCK)
                .add(Items.DEAD_HORN_CORAL_BLOCK);
        tag(VMinusTags.Items.DEAD_CORALS)
                .add(Items.DEAD_TUBE_CORAL)
                .add(Items.DEAD_BRAIN_CORAL)
                .add(Items.DEAD_BUBBLE_CORAL)
                .add(Items.DEAD_FIRE_CORAL)
                .add(Items.DEAD_HORN_CORAL);
        tag(VMinusTags.Items.DEAD_CORAL_FANS)
                .add(Items.DEAD_TUBE_CORAL_FAN)
                .add(Items.DEAD_BRAIN_CORAL_FAN)
                .add(Items.DEAD_BUBBLE_CORAL_FAN)
                .add(Items.DEAD_FIRE_CORAL_FAN)
                .add(Items.DEAD_HORN_CORAL_FAN);
        tag(VMinusTags.Items.BANNED);
        tag(VMinusTags.Items.UNCOMMON);
        tag(VMinusTags.Items.RARE);
        tag(VMinusTags.Items.EPIC);
        tag(VMinusTags.Items.LEGENDARY);
        tag(VMinusTags.Items.DELICACY);
        tag(VMinusTags.Items.INVERTED);
        tag(VMinusTags.Items.UNOBTAINABLE);
        tag(VMinusTags.Items.WOODEN_FENCE_GATES);
        tag(VMinusTags.Items.WOODEN_TOOLS)
                .add(Items.WOODEN_SWORD)
                .add(Items.WOODEN_PICKAXE)
                .add(Items.WOODEN_AXE)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.WOODEN_HOE);
        tag(VMinusTags.Items.STONE_TOOLS)
                .add(Items.STONE_SWORD)
                .add(Items.STONE_PICKAXE)
                .add(Items.STONE_AXE)
                .add(Items.STONE_SHOVEL)
                .add(Items.STONE_HOE);
        tag(VMinusTags.Items.GOLDEN_TOOLS)
                .add(Items.GOLDEN_SWORD)
                .add(Items.GOLDEN_PICKAXE)
                .add(Items.GOLDEN_AXE)
                .add(Items.GOLDEN_SHOVEL)
                .add(Items.GOLDEN_HOE);
        tag(VMinusTags.Items.IRON_TOOLS)
                .add(Items.IRON_SWORD)
                .add(Items.IRON_PICKAXE)
                .add(Items.IRON_AXE)
                .add(Items.IRON_SHOVEL)
                .add(Items.IRON_HOE);
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

        var armor = tag(VMinusTags.Items.ARMOR);
        armor.addOptionalTag(Tags.Items.ARMORS);

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

        var buckets = tag(VMinusTags.Items.BUCKETS);
        buckets.addTag(VMinusTags.Items.MOB_BUCKETS);
        buckets.addTag(VMinusTags.Items.SOLID_BUCKETS);
        buckets.addTag(VMinusTags.Items.FOOD_BUCKETS);
        buckets.addTag(VMinusTags.Items.LIQUID_BUCKETS);

        var foodBuckets = tag(VMinusTags.Items.FOOD_BUCKETS);
        foodBuckets.add(Items.MILK_BUCKET);

        var bannerPatterns = tag(VMinusTags.Items.BANNER_PATTERNS);
        var chestBoats = tag(VMinusTags.Items.CHEST_BOATS);
        var boats = tag(VMinusTags.Items.BOATS);
        var shulkerBoxes = tag(VMinusTags.Items.SHULKER_BOXES);
        var concretePowder = tag(VMinusTags.Items.CONCRETE_POWDER);
        var glazedTerracotta = tag(VMinusTags.Items.GLAZED_TERRACOTTA);

        var solidBuckets = tag(VMinusTags.Items.SOLID_BUCKETS);
        var mobBuckets = tag(VMinusTags.Items.MOB_BUCKETS);
        var liquidBuckets = tag(VMinusTags.Items.LIQUID_BUCKETS);

        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            Item item = entry.getValue();
            if (item instanceof BannerPatternItem) {
                bannerPatterns.add(item);
            } else if (item instanceof BoatItem boatItem) {
                BoatItemAccessor accessor = (BoatItemAccessor) boatItem;
                if (accessor.invokeHasChest())
                    chestBoats.add(item);
                else
                    boats.add(item);
            } else if (item instanceof SolidBucketItem) {
                solidBuckets.add(item);
            } else if (item instanceof MobBucketItem) {
                mobBuckets.add(item);
            } else if (item instanceof BucketItem) {
                liquidBuckets.add(item);
            } else if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                if (block instanceof ShulkerBoxBlock)
                    shulkerBoxes.add(item);
                else if (block instanceof ConcretePowderBlock)
                    concretePowder.add(item);
                else if (block instanceof GlazedTerracottaBlock)
                    glazedTerracotta.add(item);
            }
        }
    }
}
