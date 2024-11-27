package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;

public class PrintBlockVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Block Vision File: " + VminusModVariables.main_block_vision));
        VMinusMod.LOGGER.info(("Raw Block Vision Cache: " + VisionHandler.getBlockVisionCache()));
        VMinusMod.LOGGER.info(("Raw Block Vision Key: " + VisionHandler.getBlockVisionKey()));
    }
}
