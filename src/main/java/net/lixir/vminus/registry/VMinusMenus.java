
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.registry;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.world.inventory.CapesMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VMinusMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, VMinusMod.MODID);
    public static final RegistryObject<MenuType<CapesMenu>> CAPES_MENU = REGISTRY.register("capes_menu", () -> IForgeMenuType.create(CapesMenu::new));
}
