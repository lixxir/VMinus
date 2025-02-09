package net.lixir.vminus.core.conditions;

import net.minecraftforge.fml.ModList;

public class ModLoadedCondition implements IVisionCondition {
    @Override
    public boolean test(VisionConditionArguments visionConditionArguments, String value) {
        return ModList.get().isLoaded(value);
    }
}
