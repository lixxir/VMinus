package net.lixir.vminus.procedures;

import net.lixir.vminus.CapeHelper;
import net.minecraft.world.entity.Entity;

public class OwnsMarrowCapeProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return !CapeHelper.ownsCape(entity, "marrow");
    }
}
