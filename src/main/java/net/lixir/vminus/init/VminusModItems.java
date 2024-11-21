
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.init;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.item.DefaultItemItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VminusModItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, VminusMod.MODID);
    public static final RegistryObject<Item> DEFAULT_ITEM = REGISTRY.register("default_item", () -> new DefaultItemItem());
    public static final RegistryObject<Item> DEFAULT_BLOCK = block(VminusModBlocks.DEFAULT_BLOCK);

    // Start of user code block custom items
    // End of user code block custom items
    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
