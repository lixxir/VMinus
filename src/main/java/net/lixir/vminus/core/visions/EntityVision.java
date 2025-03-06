package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.util.VisionBaseAttribute;
import net.lixir.vminus.core.values.VisionProperty;
import net.lixir.vminus.core.values.BasicVisionValue;

public class EntityVision extends Vision<EntityVision> {
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> silent = new VisionProperty<>("silent");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> dampensVibrations = new VisionProperty<>("dampens_vibrations");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> ban = new VisionProperty<>("ban");
    public final VisionProperty<BasicVisionValue<VisionBaseAttribute>, VisionBaseAttribute> baseAttribute = new VisionProperty<>("base_attribute");

    @Override
    public void merge(EntityVision vision) {
        silent.merge(vision.silent);
        dampensVibrations.merge(vision.dampensVibrations);
        ban.merge(vision.ban);
        baseAttribute.merge(vision.baseAttribute);
    }
}
