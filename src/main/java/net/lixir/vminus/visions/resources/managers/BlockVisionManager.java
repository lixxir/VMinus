package net.lixir.vminus.visions.resources.managers;

import net.lixir.vminus.visions.VisionType;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.accessors.IBlockVisionAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class BlockVisionManager extends VisionManager<BlockVision> {
    private static final String DIRECTORY = VisionType.BLOCK.getDirectoryName();

    public BlockVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, BlockVision.class, new VisionDeserializer<>(BlockVision.class, context));
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
            if (block instanceof IBlockVisionAccessor visionable) {
                visionable.vminus$setVision(copyVision);
            }
        }
    }
}
