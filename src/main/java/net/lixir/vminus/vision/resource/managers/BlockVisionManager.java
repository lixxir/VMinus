package net.lixir.vminus.vision.resource.managers;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;

@SuppressWarnings("deprecation")
public class BlockVisionManager extends VisionManager<Block> {
    public BlockVisionManager(ICondition.IContext context) {
        super(context, VisionTypes.BLOCK);
    }

    @Override
    protected void applyVisions(List<VisionEntry<Block>> visionEntries) {
        super.applyVisions(visionEntries);

        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

            VisionEntry<Block> mergedEntry = new VisionEntry<>();
            for (VisionEntry<Block> visionEntry : visionEntries) {
                if (VisionEntry.visionApplies(block, id.toString(), visionEntry.getEntries(), this.getContext())) {
                    mergedEntry.merge(visionEntry);
                }
            }

            int index;
            if (mergedEntry.isEmpty()) {
                index = 0;
            } else {
                VMinus.LOGGER.info("Id={}, Entry={}", id, mergedEntry);
                Vision vision = Vision.fromEntry(mergedEntry);
                index = Vision.getOrAddVisionIndex(vision);
            }


            visionIndexes.put(id, index);
            ((VisionDuck)block).vMinus$setVisionIndex(index);
        }
    }
}
