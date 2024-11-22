package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEnchantmentVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Enchantment Vision File: " + VminusModVariables.main_enchantment_vision));
    }
}
