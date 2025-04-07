package net.lixir.vminus.visions.conditions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

public class EntityStringNbtVisionCondition extends AbstractVisionCondition {
    private final String path;
    private final String value;

    public EntityStringNbtVisionCondition(String path, String value, boolean inverted) {
        super(inverted);
        this.path = path;
        this.value = value;
    }
    @Override
    public boolean test(VisionConditionArguments visionConditionArguments) {
        if (visionConditionArguments.hasEntity()) {
            Entity entity = visionConditionArguments.getEntity();
            assert entity != null;
            CompoundTag nbt = entity.getPersistentData();
            Tag tag = getTagFromPath(nbt, path.split("/"));
            return (tag != null && tag.getAsString().equals(value));
        }
        return true;
    }

    private Tag getTagFromPath(CompoundTag nbt, String[] path) {
        Tag current = nbt;
        for (String key : path) {
            if (current instanceof CompoundTag compound) {
                current = compound.get(key);
            } else {
                return null;
            }
            if (current == null) {
                return null;
            }
        }

        return current;
    }


}
