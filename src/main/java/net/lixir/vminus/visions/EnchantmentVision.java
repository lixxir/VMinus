package net.lixir.vminus.visions;

import net.lixir.vminus.visions.accessors.EnchantmentVisionAccessor;
import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class EnchantmentVision extends Vision {
    public static final EnchantmentVision EMPTY = new EnchantmentVision();

    public final VisionProperty<Boolean> ban;

    public EnchantmentVision() {
        ban = create("ban", VisionCodecs.booleanCodec());
    }

    public static @NotNull EnchantmentVision of(Item item) {
        if (item instanceof EnchantmentVisionAccessor enchantmentVisionAccessor)
            return enchantmentVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.ENCHANTMENT.getListName();
    }
}
