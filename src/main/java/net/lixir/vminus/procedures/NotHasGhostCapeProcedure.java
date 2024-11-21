package net.lixir.vminus.procedures;

import net.lixir.vminus.network.VminusModVariables;
import net.minecraft.world.entity.Entity;

public class NotHasGhostCapeProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return !((entity.getCapability(VminusModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new VminusModVariables.PlayerVariables())).cape_id).equals("ghost");
    }
}
