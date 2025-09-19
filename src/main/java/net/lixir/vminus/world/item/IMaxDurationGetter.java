package net.lixir.vminus.world.item;

public interface IMaxDurationGetter {
    default int vminus$getMaxDuration() {
        return 0;
    }
}
