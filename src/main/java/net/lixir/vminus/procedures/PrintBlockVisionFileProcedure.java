package net.lixir.vminus.procedures;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintBlockVisionFileProcedure {
    public static void execute() {
        VminusMod.LOGGER.debug(("Raw Block Vision File: " + VminusModVariables.main_block_vision));
    }
}
