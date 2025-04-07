package net.lixir.vminus.datagen.util.simple;

import net.lixir.vminus.registry.util.BlockItemRegistryPair;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class OreDatagen extends BlockItemDatagen {

    private final RegistryObject<Item> oreDrop;

    public OreDatagen(BlockItemRegistryPair blockItemRegistryPair, RegistryObject<Item> oreDrop) {
        super(blockItemRegistryPair, Type.ORE, new Flags());
        this.oreDrop = oreDrop;
    }

    public OreDatagen(BlockItemRegistryPair blockItemRegistryPair, RegistryObject<Item> oreDrop, Flags flags) {
        super(blockItemRegistryPair, Type.ORE, flags);
        this.oreDrop = oreDrop;
    }

    public RegistryObject<Item> getOreDrop() {
        return oreDrop;
    }
}
