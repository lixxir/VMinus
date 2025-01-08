package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;

public class VMinusBlockTagGenerator extends BlockTagsProvider {
    public VMinusBlockTagGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, VMinusMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<Block> torches = tag(VMinusTags.Blocks.TORCHES);
        torches.add(Blocks.TORCH, Blocks.WALL_TORCH);

        TagAppender<Block> soul_torches = tag(VMinusTags.Blocks.SOUL_TORCHES);
        soul_torches.add(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);

        TagAppender<Block> mob_heads = tag(VMinusTags.Blocks.MOB_HEADS);
        TagAppender<Block> concrete_powder = tag(VMinusTags.Blocks.CONCRETE_POWDER);

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block instanceof ConcretePowderBlock) {
                concrete_powder.add(block);
            } else if (block instanceof SkullBlock || block instanceof WallSkullBlock) {
                mob_heads.add(block);
            }
        }
    }
}
