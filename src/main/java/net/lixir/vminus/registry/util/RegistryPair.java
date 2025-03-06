package net.lixir.vminus.registry.util;

import net.minecraftforge.registries.RegistryObject;

public class RegistryPair<A,B> {
    protected final RegistryObject<A> firstRegistryObject;
    protected final RegistryObject<B> secondRegistryObject;

    public RegistryPair(RegistryObject<A> firstRegistryObject, RegistryObject<B> secondRegistryObject) {
        this.firstRegistryObject = firstRegistryObject;
        this.secondRegistryObject = secondRegistryObject;
    }
}
