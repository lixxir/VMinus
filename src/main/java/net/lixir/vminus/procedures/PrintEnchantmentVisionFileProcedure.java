package net.lixir.vminus.procedures;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEnchantmentVisionFileProcedure {
    public static void execute() {
        VminusMod.LOGGER.debug(("Raw Enchantment Vision File: " + VminusModVariables.main_enchantment_vision));
    }
}
