package net.lixir.vminus.visions.resources;

import net.lixir.vminus.visions.accessors.VisionAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class VisionResourceController {
    public static void clearVisions() {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$clearVision();
            }
        }
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$clearVision();
            }
        }
        for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
            if (entityType instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$clearVision();
            }
        }
        for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS) {
            if (mobEffect instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$clearVision();
            }
        }
        for (CreativeModeTab tab : BuiltInRegistries.CREATIVE_MODE_TAB) {
            if (tab instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$clearVision();
            }
        }
    }

    public static void freezeVisions() {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$freezeVision();
            }
        }
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$freezeVision();
            }
        }
        for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
            if (entityType instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$freezeVision();
            }
        }
        for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS) {
            if (mobEffect instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$freezeVision();
            }
        }
        for (CreativeModeTab tab : BuiltInRegistries.CREATIVE_MODE_TAB) {
            if (tab instanceof VisionAccessor<?> visionAccessor) {
                visionAccessor.vminus$freezeVision();
            }
        }
    }
}
