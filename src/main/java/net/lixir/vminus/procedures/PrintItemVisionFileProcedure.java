package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;

public class PrintItemVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.info(("Raw Item Vision File: " + VminusModVariables.main_item_vision));
        VMinusMod.LOGGER.info(("Raw Item Vision Cache: " + VisionHandler.getItemVisionCache()));
        VMinusMod.LOGGER.info(("Raw Item Vision Key: " + VisionHandler.getItemVisionKey()));
    }
}
