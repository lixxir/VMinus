package net.lixir.vminus.vision.resource.managers;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionEntry;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;

@SuppressWarnings("deprecation")
public class ItemVisionManager extends VisionManager<Item> {
    public ItemVisionManager(ICondition.IContext context) {
        super(context, VisionTypes.ITEM);
    }

    @Override
    protected void applyVisions(List<VisionEntry<Item>> visionEntries) {
        super.applyVisions(visionEntries);

        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);

            VisionEntry<Item> mergedEntry = new VisionEntry<>();
            for (VisionEntry<Item> visionEntry : visionEntries) {
                if (VisionEntry.visionApplies(item, id.toString(), visionEntry.getEntries(), this.getContext())) {
                    mergedEntry.merge(visionEntry);
                }
            }

            int index;
            if (mergedEntry.isEmpty()) {
                index = 0;
            } else {

                Vision vision = Vision.fromEntry(mergedEntry);
                index = Vision.getOrAddVisionIndex(vision);
            }

            visionIndexes.put(id, index);
            ((VisionDuck)item).vMinus$setVisionIndex(index);
        }
    }
}
