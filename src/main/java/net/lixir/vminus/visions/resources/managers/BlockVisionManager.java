package net.lixir.vminus.visions.resources.managers;

import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.accessors.BlockVisionAccessor;
import net.lixir.vminus.visions.values.VisionValue;
import net.minecraft.world.item.Item;
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
            if (block instanceof BlockVisionAccessor visionable) {
                visionable.vminus$mergeVision(copyVision);
                // Attempt to ban block item counterparts if the block is banned
                if (Boolean.TRUE.equals(copyVision.ban.value())) {
                    Item item = block.asItem();
                    if (item instanceof ItemVisionAccessor itemVisionAccessor) {
                        ItemVision itemVision = new ItemVision();
                        itemVision.ban.add(new VisionValue<>(true, List.of()));
                        itemVisionAccessor.vminus$mergeVision(itemVision);
                    }
                }
            }
        }
    }
}
