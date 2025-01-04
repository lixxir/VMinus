package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.util.VisionType;

public class PrintItemVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Item Vision File: " + VisionType.ITEM.getMainVision()));
        VMinusMod.LOGGER.info(("Raw Item Vision Cache: " + VisionType.ITEM.getVisionCache()));
        VMinusMod.LOGGER.info(("Raw Item Vision Key: " + VisionType.ITEM.getVisionKey()));
    }
}
