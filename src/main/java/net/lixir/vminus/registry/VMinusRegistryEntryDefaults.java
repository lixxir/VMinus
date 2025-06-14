package net.lixir.vminus.registry;

import net.lixir.vminus.block.LogBlock;
import net.lixir.vminus.registry.entry.BlockEntry;
import net.lixir.vminus.registry.entry.ItemEntry;
import net.lixir.vminus.registry.entry.RegistryEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VMinusRegistryEntryDefaults {
    public static void init(){
    }

    static {
        register(AbstractGlassBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .tags(List.of(Tags.Blocks.GLASS))
                        .model(BlockModel.CUBE_ALL)
                        .renderType("cutout")

        ));
        register(LogBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .tags(List.of(BlockTags.LOGS))
                        .model(BlockModel.AXIS)
                        .renderType("solid")

        ));
        register(StainedGlassBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .tags(List.of(Tags.Blocks.GLASS))
                        .model(BlockModel.CUBE_ALL)
                        .renderType("translucent")

        ));
        register(FungusBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .tintType(TintType.EMPTY)
        ));
        register(MushroomBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .tintType(TintType.EMPTY)
        ));
        register(BaseFireBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
        ));
        register(WallTorchBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
        ));
        register(TorchBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
        ));
        register(HangingRootsBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .model(BlockModel.CROSS)
        ));
        register(DoublePlantBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .tintType(TintType.GRASS)
                        .model(BlockModel.DOUBLE_CROSS)
        ));
        register(BushBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .tintType(TintType.GRASS)
                        .model(BlockModel.CROSS)
        ));
        register(FlowerBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .tags(List.of(BlockTags.SMALL_FLOWERS))
                        .model(BlockModel.CROSS)
        ));
        register(RootsBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .model(BlockModel.CROSS)
        ));
        register(LanternBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .renderType("cutout")
                        .model(BlockModel.EMPTY)
        ));
        register(Item.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                ItemEntry.of()
                        .model(ItemModel.BASIC)
        ));
        register(LeavesBlock.class, new UnifiedRegistry.DefaultRegistryEntry<>(
                BlockEntry.of()
                        .tags(List.of(BlockTags.LEAVES))
                        .tintType(TintType.FOLIAGE)
                        .renderType("cutout_mipped")
                        .model(BlockModel.TINTED_CUBE_ALL)
        ));
    }


    private static <E extends RegistryEntry<E, T>, T> void register(@NotNull Class<?> clazz, @NotNull UnifiedRegistry.DefaultRegistryEntry<E, T> defaultRegistryEntry) {
        UnifiedRegistry.setDefaultRegistryEntry(clazz, defaultRegistryEntry);
    }
}
