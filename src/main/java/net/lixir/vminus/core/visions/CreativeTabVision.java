package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.util.VisionCreativeOrder;
import net.lixir.vminus.core.util.VisionItemStackWithTagKey;
import net.lixir.vminus.core.values.BasicVisionValue;
import net.lixir.vminus.core.values.VisionProperty;
import net.lixir.vminus.core.visions.accessors.ICreativeTabVisionAccessor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabVision extends Vision<CreativeTabVision> {
    private static final CreativeTabVision emptyVision = new CreativeTabVision();

    public final VisionProperty<BasicVisionValue<VisionCreativeOrder>, VisionCreativeOrder> order = new VisionProperty<>("order");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> hide = new VisionProperty<>("hide");
    public final VisionProperty<BasicVisionValue<ItemStack>, ItemStack> icon = new VisionProperty<>("icon");
    public final VisionProperty<BasicVisionValue<VisionItemStackWithTagKey>, VisionItemStackWithTagKey> remove = new VisionProperty<>("remove");

    @Override
    public void merge(CreativeTabVision vision) {
        order.merge(vision.order);
        hide.merge(vision.hide);
        icon.merge(vision.icon);
        remove.merge(vision.remove);
    }


    public static CreativeTabVision getVision(CreativeModeTab creativeModeTab) {
        if (creativeModeTab instanceof ICreativeTabVisionAccessor visionable)
            return visionable.vminus$getVision();
        return emptyVision;
    }
}
