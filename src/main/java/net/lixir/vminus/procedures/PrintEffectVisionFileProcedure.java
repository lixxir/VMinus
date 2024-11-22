package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEffectVisionFileProcedure {
    public static void execute() {
        VMinusMod.LOGGER.debug(("Raw Effect Vision File: " + VminusModVariables.main_effect_vision));
    }
}
