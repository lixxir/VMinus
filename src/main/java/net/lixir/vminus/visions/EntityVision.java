package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.resources.codec.VisionBaseAttributeCodec;
import net.lixir.vminus.visions.resources.codec.VisionEntityVariantCodec;
import net.lixir.vminus.visions.util.VisionBaseAttribute;
import net.lixir.vminus.visions.util.VisionEntityVariant;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.EntityVisionAccessor;
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
        silent = create("silent", VisionCodecs.booleanCodec());
        dampens_vibration = create("dampens_vibration", VisionCodecs.booleanCodec());
        ban = create("ban", VisionCodecs.booleanCodec());
        base_attribute = create("base_attribute", new VisionBaseAttributeCodec(), true);
        variant = create("variant", new VisionEntityVariantCodec(), true);
    }

    public static @NotNull EntityVision of(@Nullable Entity entity) {
        if (entity == null)
            return EMPTY;
        if (entity.getType() instanceof EntityVisionAccessor entityVisionAccessor)
            return entityVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.ENTITY.getListName();
    }
}
