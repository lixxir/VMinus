package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.parser.*;
import net.lixir.vminus.visions.util.VisionCreativeOrder;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.ICreativeTabVisionAccessor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabVision extends Vision {
    private static final CreativeTabVision EMPTY = new CreativeTabVision();

    public final VisionProperty<VisionCreativeOrder> order;
    public final VisionProperty<Boolean> hide;
    public final VisionProperty<ItemStack> icon;
    public final VisionProperty<VisionItemReplacement> remove;


    public CreativeTabVision() {
        order = create("order", new VisionCreativeOrderParser());
        hide = create("hide", new VisionBooleanParser());
        icon = create("icon", new VisionItemStackParser());
        remove = create("remove", new VisionItemReplacementParser());
    }

    public static CreativeTabVision of(CreativeModeTab creativeModeTab) {
        if (creativeModeTab instanceof ICreativeTabVisionAccessor visionable)
            return visionable.vminus$getVision();
        return EMPTY;
    }
}
