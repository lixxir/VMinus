package net.lixir.vminus.visions.resources.managers;

import net.lixir.vminus.visions.EffectVision;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.accessors.EffectVisionAccessor;
import net.lixir.vminus.visions.resources.VisionDeserializer;
import net.lixir.vminus.visions.resources.VisionProcessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class EffectVisionManager extends VisionManager<EffectVision> {
    private static final String DIRECTORY = VisionType.EFFECT.getDirectoryName();

    public EffectVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, EffectVision.class, new VisionDeserializer<>(EffectVision.class, context));
    }

    @Override
    protected void applyVisions(List<EffectVision> visions) {
        for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS.getValues()) {
            final String id = Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(mobEffect)).toString();
            EffectVision copyVision = new EffectVision();
            for (EffectVision vision : visions) {
                if (VisionProcessor.visionApplies(mobEffect, id, vision.getEntries(), this.getContext())) {
                    copyVision.merge(vision);
                }
            }
            if (mobEffect instanceof EffectVisionAccessor effectVisionAccessor) {
                effectVisionAccessor.vminus$mergeVision(copyVision);
            }
        }
    }
}
