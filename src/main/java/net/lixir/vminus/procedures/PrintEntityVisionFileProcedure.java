package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;

public class PrintEntityVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Entity Vision File: " + VisionType.ENTITY.getMainVision()));
        VMinusMod.LOGGER.info(("Raw Entity Vision Cache: " + VisionType.ENTITY.getVisionCache()));
        VMinusMod.LOGGER.info(("Raw Entity Vision Key: " + VisionType.ENTITY.getVisionKey()));
    }
}
