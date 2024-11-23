package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;

public class PrintEntityVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.info(("Raw Entity Vision File: " + VminusModVariables.main_entity_vision));
        VMinusMod.LOGGER.info(("Raw Entity Vision Cache: " + VisionHandler.getEntityVisionCache()));
        VMinusMod.LOGGER.info(("Raw Entity Vision Key: " + VisionHandler.getEntityVisionKey()));
    }
}
