package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.block.VMinusBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.lixir.vminus.VMinus.REGISTRY;

public class VMinusSounds {
	public static void init() {}
	public static final SoundEvent ITEM_DROP = REGISTRY.sound("ui.inventory.item.drop", "ui/inventory/item/drop.opus");
}
