package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.vision.VisionProperties;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.vision.util.ItemReplacement;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class VMinusVisionProvider extends VisionProvider {
    public VMinusVisionProvider(PackOutput output) {
        super(output, VMinus.ID);
    }

    @Override
    protected void addVisions() {
        vision(VisionTypes.ITEM)
                .with(VisionProperties.Items.REPLACE, ItemReplacement.of(Items.OBSIDIAN))
                .isFor("minecraft:diamond_helmet")
                .save(this, new ResourceLocation(VMinus.ID, "chopped"));
    }
}
