package net.lixir.vminus.procedures;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEntityVisionFileProcedure {
    public static void execute() {
        VminusMod.LOGGER.debug(("Raw Entity Vision File: " + VminusModVariables.main_entity_vision));
    }
}
