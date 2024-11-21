package net.lixir.vminus.procedures;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class HealProcedureProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments, Entity entity) {
        if (entity == null)
            return;
        String currentDimension = "";
        currentDimension = entity.level().dimension().location().toString();
        try {
            for (Entity entityiterator : EntityArgument.getEntities(arguments, "entities")) {
                if (world instanceof ServerLevel _origLevel) {
                    LevelAccessor _worldorig = world;
                    world = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(currentDimension)));
                    if (world != null) {
                        if (entity instanceof LivingEntity _entity)
                            _entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + DoubleArgumentType.getDouble(arguments, "health")));
                    }
                }
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        if (false) {
            if (world instanceof Level _level && !_level.isClientSide())
                _level.explode(null, x, y, z, 4, Level.ExplosionInteraction.NONE);
        }
    }
}
