package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintBlockVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Block Vision File: " + VminusModVariables.main_block_vision));
    }
}
