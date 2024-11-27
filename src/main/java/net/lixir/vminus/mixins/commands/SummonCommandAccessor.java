package net.lixir.vminus.mixins.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SummonCommand.class)
public interface SummonCommandAccessor {
    @Invoker("spawnEntity")
    static int invokeSpawnEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> entityType, Vec3 position, CompoundTag nbt, boolean booleanFlag) throws CommandSyntaxException {
        throw new AssertionError();
    }
}
