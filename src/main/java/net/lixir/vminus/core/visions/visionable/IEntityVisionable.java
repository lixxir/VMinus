package net.lixir.vminus.core.visions.visionable;

import net.lixir.vminus.core.visions.BlockVision;
import net.lixir.vminus.core.visions.EntityVision;

public interface IEntityVisionable {
    EntityVision vminus$getVision();

    void vminus$setVision(EntityVision vision);
}
