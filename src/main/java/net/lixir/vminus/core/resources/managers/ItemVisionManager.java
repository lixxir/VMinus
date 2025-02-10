package net.lixir.vminus.core.resources.managers;

import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.visions.ItemVision;
import net.lixir.vminus.core.visions.visionable.IItemVisionable;
import net.lixir.vminus.core.resources.deserializers.ItemVisionDeserializer;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class ItemVisionManager extends VisionManager<ItemVision> {
    private static final String DIRECTORY = VisionType.ITEM.getDirectoryName();

    public ItemVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, ItemVision.class, new ItemVisionDeserializer());
    }

    @Override
    protected void applyVisions(List<ItemVision> itemVisions) {
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            final String id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();
            ItemVision copyVision = new ItemVision();
            for (ItemVision vision : itemVisions) {
                if (VisionProcessor.visionApplies(item, id, vision.getEntries(), this.getContext())) {
                    copyVision.merge(vision);
                }
            }
            if (item instanceof IItemVisionable itemVisionable) {
                itemVisionable.vminus$setVision(copyVision);
            }
        }
    }
}
