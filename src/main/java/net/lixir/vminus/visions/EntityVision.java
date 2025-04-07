package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.parser.VisionBaseAttributeParser;
import net.lixir.vminus.visions.resources.parser.VisionBooleanParser;
import net.lixir.vminus.visions.resources.parser.VisionEntityVariantParser;
import net.lixir.vminus.visions.resources.parser.VisionIntParser;
import net.lixir.vminus.visions.util.VisionBaseAttribute;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.IEntityVisionAccessor;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityVision extends Vision {
    public static final EntityVision EMPTY = new EntityVision();

    public final VisionProperty<Boolean> silent;
    public final VisionProperty<Boolean> dampens_vibration;
    public final VisionProperty<Boolean> ban;
    public final VisionProperty<VisionBaseAttribute> base_attribute;
    public final VisionProperty<VisionEntityVariant> variant;

    public EntityVision() {
        silent = create("silent", new VisionBooleanParser());
        dampens_vibration = create("dampens_vibration", new VisionBooleanParser());
        ban = create("ban", new VisionBooleanParser());
        base_attribute = create("base_attribute", new VisionBaseAttributeParser());
        variant = create("variant", new VisionEntityVariantParser());
    }

    public static @NotNull EntityVision of(@Nullable Entity entity) {
        if (entity == null)
            return EMPTY;
        if (entity.getType() instanceof IEntityVisionAccessor entityVisionAccessor)
            return entityVisionAccessor.vminus$getVision();
        return EMPTY;
    }
}
