package net.lixir.vminus.mixins.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SummonCommand.class)
public interface SummonCommandAccessor {
    @Invoker("spawnEntity")
    static int invokeSpawnEntity(CommandSourceStack source, ResourceLocation entityType, Vec3 position, CompoundTag nbt, boolean flag) {
        throw new AssertionError(); // This will never be called; it's just a placeholder
    }
}
