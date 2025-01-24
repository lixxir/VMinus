
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VMinusSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, VMinus.ID);

	public static final RegistryObject<SoundEvent> ITEM_DROP = SOUNDS.register("ui.inventory.item.drop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(VMinus.ID, "ui.inventory.item.drop")));
}
