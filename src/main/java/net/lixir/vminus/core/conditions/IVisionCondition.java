package net.lixir.vminus.core.conditions;

public interface IVisionCondition {
    boolean test(VisionConditionArguments visionConditionArguments, String value);
}
