package net.lixir.vminus.procedures;

import net.lixir.vminus.VMinusMod;
import net.lixir.vminus.helpers.CapeHelper;
import net.minecraft.world.entity.Entity;

public class OwnsBeeperCapeProcedure {
    public static boolean execute(Entity entity) {
        if (entity == null)
            return false;
        if (false) {
            VMinusMod.LOGGER.info(entity);
        }
        return !CapeHelper.ownsCape(entity, "beeper");
    }
}
