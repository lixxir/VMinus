package net.lixir.vminus.core.resources.managers;

import net.lixir.vminus.core.VisionType;
import net.lixir.vminus.core.resources.VisionProcessor;
import net.lixir.vminus.core.resources.deserializers.EntityVisionDeserializer;
import net.lixir.vminus.core.visions.EntityVision;
import net.lixir.vminus.core.visions.accessors.IEntityVisionAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class EntityVisionManager extends VisionManager<EntityVision> {
    private static final String DIRECTORY = VisionType.ENTITY.getDirectoryName();

    public EntityVisionManager(ICondition.IContext context) {
        super(context, DIRECTORY, EntityVision.class, new EntityVisionDeserializer());
    }

    @Override
    protected void applyVisions(List<EntityVision> visions) {
        for (EntityType<?> entity : ForgeRegistries.ENTITY_TYPES.getValues()) {
            final String id = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity)).toString();
            EntityVision copyVision = new EntityVision();
            for (EntityVision vision : visions) {
                if (VisionProcessor.visionApplies(entity, id, vision.getEntries(), this.getContext())) {
                    copyVision.merge(vision);
                }
            }
            if (entity instanceof IEntityVisionAccessor visionable) {
                visionable.vminus$setVision(copyVision);
            }
        }
    }
}
