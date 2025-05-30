package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.util.VisionCreativeOrder;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.CreativeTabVisionAccessor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabVision extends Vision {
    public static final CreativeTabVision EMPTY = new CreativeTabVision();

    public final VisionProperty<VisionCreativeOrder> order;
    public final VisionProperty<Boolean> hide;
    public final VisionProperty<ItemStack> icon;
    public final VisionProperty<VisionItemReplacement> remove;


    public CreativeTabVision() {
        order = create("order", VisionCodecs.creativeOrderCodec(), true);
        hide = create("hide", VisionCodecs.booleanCodec());
        icon = create("icon", VisionCodecs.itemStackCodec());
        remove = create("remove", VisionCodecs.itemReplacementCodec(), true);
    }

    public static CreativeTabVision of(CreativeModeTab creativeModeTab) {
        if (creativeModeTab instanceof CreativeTabVisionAccessor visionable)
            return visionable.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.CREATIVETAB.getListName();
    }
}
