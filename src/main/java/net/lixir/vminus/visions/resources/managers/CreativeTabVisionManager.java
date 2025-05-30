package net.lixir.vminus.visions.resources.managers;

import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.lixir.vminus.visions.CreativeTabVision;
import net.lixir.vminus.visions.accessors.CreativeTabVisionAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreativeTabVisionManager extends VisionManager<CreativeTabVision> {
    private static final String DIRECTORY = VisionType.CREATIVETAB.getDirectoryName();

    public CreativeTabVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, CreativeTabVision.class, new VisionDeserializer<>(CreativeTabVision.class, context));
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
            if (creativeModeTab instanceof CreativeTabVisionAccessor visionable) {
                visionable.vminus$mergeVision(copyVision);
            }
        }
    }
}
