package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.VisionProperty;
import net.lixir.vminus.core.values.BasicVisionValue;

public class EntityVision extends Vision<EntityVision> {
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> silent = new VisionProperty<>("silent");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> dampensVibrations = new VisionProperty<>("dampens_vibrations");

    @Override
    public void merge(EntityVision vision) {
        silent.mergeValues(vision.silent);
    }
}
