package net.lixir.vminus.resources.data;

import net.minecraft.core.RegistryAccess;

public interface RegistryAccessHolder {
    RegistryAccess.Frozen vMinus$getRegistryAccess();
    void vMinus$setRegistryAccess(RegistryAccess.Frozen access);
}
