package net.lixir.vminus.procedures;

import net.lixir.vminus.core.VisionHandler;
import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintItemVisionFileProcedure {
    public static void execute() {
        VminusMod.LOGGER.info(("Raw Item Vision File: " + VminusModVariables.main_item_vision));
        VminusMod.LOGGER.info(("Raw Item Vision Cache: " + VisionHandler.getItemVisionCache()));
        VminusMod.LOGGER.info(("Raw Item Vision Key: " + VisionHandler.getItemVisionKey()));
    }
}
