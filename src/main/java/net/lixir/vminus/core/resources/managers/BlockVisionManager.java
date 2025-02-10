package net.lixir.vminus.core.resources.managers;

import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.resources.deserializers.BlockVisionDeserializer;
import net.lixir.vminus.core.resources.deserializers.ItemVisionDeserializer;
import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IBlockVisionable;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class BlockVisionManager extends VisionManager<BlockVision> {
    private static final String DIRECTORY = VisionType.BLOCK.getDirectoryName();

    public BlockVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, BlockVision.class, new BlockVisionDeserializer());
    }

    @Override
    protected void applyVisions(List<BlockVision> visions) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            final String id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString();
            BlockVision copyVision = new BlockVision();
            for (BlockVision vision : visions) {
                if (VisionProcessor.visionApplies(block, id, vision.getEntries(), this.getContext())) {
                    copyVision.merge(vision);
                }
            }
            if (block instanceof IBlockVisionable visionable) {
                visionable.vminus$setVision(copyVision);
            }
        }
    }
}
