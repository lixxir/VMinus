
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.lixir.vminus.init;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.world.inventory.CapesMenuMenu;
import net.lixir.vminus.world.inventory.DefaultGuiMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VminusModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, VminusMod.MODID);
    public static final RegistryObject<MenuType<DefaultGuiMenu>> DEFAULT_GUI = REGISTRY.register("default_gui", () -> IForgeMenuType.create(DefaultGuiMenu::new));
    public static final RegistryObject<MenuType<CapesMenuMenu>> CAPES_MENU = REGISTRY.register("capes_menu", () -> IForgeMenuType.create(CapesMenuMenu::new));
}
