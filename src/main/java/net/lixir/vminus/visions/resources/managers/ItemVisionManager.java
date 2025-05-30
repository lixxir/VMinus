package net.lixir.vminus.visions.resources.managers;

import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.values.VisionValue;
import net.lixir.vminus.visions.BlockVision;
import net.lixir.vminus.visions.ItemVision;
import net.lixir.vminus.visions.accessors.BlockVisionAccessor;
import net.lixir.vminus.visions.accessors.ItemVisionAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class ItemVisionManager extends VisionManager<ItemVision> {
    private static final String DIRECTORY = VisionType.ITEM.getDirectoryName();

    public ItemVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, ItemVision.class, new VisionDeserializer<>(ItemVision.class, context));
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
            if (item instanceof ItemVisionAccessor itemVisionable) {
                itemVisionable.vminus$mergeVision(copyVision);
                if (Boolean.TRUE.equals(copyVision.ban.value())) {
                    Block block = ForgeRegistries.BLOCKS.getValue(ForgeRegistries.ITEMS.getKey(item));
                    if (block == null)
                        continue;
                    if (block instanceof BlockVisionAccessor blockVisionable) {
                        BlockVision blockVision = new BlockVision();
                        blockVision.ban.add(new VisionValue<>(true, List.of()));
                        blockVisionable.vminus$mergeVision(blockVision);
                    }
                }
            }
        }
    }
}
