package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;

public class PrintBlockVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Block Vision File: " + VisionType.BLOCK.getMainVision()));
        VMinusMod.LOGGER.info(("Raw Block Vision Cache: " + VisionType.BLOCK.getVisionCache()));
        VMinusMod.LOGGER.info(("Raw Block Vision Key: " + VisionType.BLOCK.getVisionKey()));
    }
}
