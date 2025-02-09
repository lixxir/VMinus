package net.lixir.vminus.core.values;

import net.lixir.vminus.core.conditions.IVisionCondition;
import net.lixir.vminus.core.conditions.VisionConditionArguments;

import javax.annotation.Nullable;
import java.util.List;

public class VisionValue {
    private final List<ConditionEntry> conditions;
    private int priority = 0;

    public VisionValue(List<ConditionEntry> conditions) {
        this.conditions = conditions;
    }

    public List<ConditionEntry> getConditions() {
        return conditions;
    }

    public int getPriority() {
        return priority;
    }

    public boolean testConditions(@Nullable VisionConditionArguments args) {
        if (args == null || conditions.isEmpty())
            return true;
        for (ConditionEntry entry : conditions) {
            if (!entry.condition().test(args, entry.value())) {
                return false;
            }
        }
        return true;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public record ConditionEntry(IVisionCondition condition, String value) {}
}
