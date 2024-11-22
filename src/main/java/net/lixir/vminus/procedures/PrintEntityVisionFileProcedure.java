package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEntityVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Entity Vision File: " + VminusModVariables.main_entity_vision));
    }
}
