package net.lixir.vminus.procedures;

import net.lixir.vminus.VminusMod;
import net.lixir.vminus.network.VminusModVariables;

public class PrintEffectVisionFileProcedure {
    public static void execute() {
        VminusMod.LOGGER.debug(("Raw Effect Vision File: " + VminusModVariables.main_effect_vision));
    }
}
