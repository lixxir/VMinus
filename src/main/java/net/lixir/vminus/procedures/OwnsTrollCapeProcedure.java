package net.lixir.vminus.procedures;

import net.lixir.vminus.helpers.CapeHelper;
import net.minecraft.world.entity.Entity;

public class OwnsTrollCapeProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        return !CapeHelper.ownsCape(entity, "troll");
    }
}
