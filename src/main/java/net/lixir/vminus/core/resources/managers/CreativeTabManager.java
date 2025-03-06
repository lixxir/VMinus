package net.lixir.vminus.core.resources.managers;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.resources.deserializers.CreativeTabVisionDeserializer;
import net.lixir.vminus.core.visions.CreativeTabVision;
import net.lixir.vminus.core.visions.accessors.ICreativeTabVisionAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreativeTabManager extends VisionManager<CreativeTabVision> {
    private static final String DIRECTORY = VisionType.CREATIVE_TAB.getDirectoryName();

    public CreativeTabManager(ICondition.IContext context) {
        super(context, DIRECTORY, CreativeTabVision.class, new CreativeTabVisionDeserializer());
    }

    @Override
    protected void applyVisions(List<CreativeTabVision> visions) {
        for (Map.Entry<ResourceKey<CreativeModeTab>, CreativeModeTab> entry : BuiltInRegistries.CREATIVE_MODE_TAB.entrySet()) {
            CreativeModeTab creativeModeTab = entry.getValue();
            final String id = Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(creativeModeTab)).toString();
            CreativeTabVision copyVision = new CreativeTabVision();
            for (CreativeTabVision vision : visions) {
                if (VisionProcessor.visionApplies(creativeModeTab, id, vision.getEntries(), this.getContext())) {
                    copyVision.merge(vision);
                }
            }
            if (creativeModeTab instanceof ICreativeTabVisionAccessor visionable) {
                VMinus.LOGGER.info("SET CREATIVE TAB VISION FOR: {}", id);
                visionable.vminus$setVision(copyVision);
            }
        }
    }
}
