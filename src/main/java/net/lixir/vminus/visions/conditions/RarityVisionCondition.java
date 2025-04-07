package net.lixir.vminus.visions.conditions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RarityVisionCondition extends AbstractVisionCondition {
    private final Rarity rarity;

    public RarityVisionCondition(Rarity rarity, boolean inverted) {
        super(inverted);
        this.rarity = rarity;
    }

    @Override
    public boolean test(VisionConditionArguments visionConditionArguments) {
        if (visionConditionArguments.hasItem()) {
            Item item = visionConditionArguments.getItem();
            assert item != null;
            return item.getRarity(item.getDefaultInstance()).equals(rarity);
        }
        return true;
    }
}
