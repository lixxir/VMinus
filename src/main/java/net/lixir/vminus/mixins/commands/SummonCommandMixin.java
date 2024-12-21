package net.lixir.vminus.mixins.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import net.lixir.vminus.visions.VisionHandler;
import net.lixir.vminus.visions.VisionValueHandler;
import net.minecraft.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummonCommand.class)
public abstract class SummonCommandMixin {

    @Inject(method = "register", at = @At(value = "HEAD"), cancellable = true)
    private static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, CallbackInfo ci) {
        dispatcher.register(Commands.literal("summon").requires((source) -> {
            return source.hasPermission(2);
        }).then(Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
                .suggests((commandContext, builder) -> {

                    return SharedSuggestionProvider.suggestResource(
                            BuiltInRegistries.ENTITY_TYPE.stream()
                                    .filter(entityType -> {
                                        JsonObject visionData = VisionHandler.getVisionData(entityType);
                                        return !VisionValueHandler.isBooleanMet(visionData, "banned", entityType);
                                    }),
                            builder,
                            EntityType::getKey,
                            entityType -> Component.translatable(Util.makeDescriptionId("entity", EntityType.getKey(entityType)))
                    );

                }).executes((commandContext) -> {
                    return SummonCommandAccessor.invokeSpawnEntity(
                            commandContext.getSource(),
                            ResourceArgument.getSummonableEntityType(commandContext, "entity"),
                            commandContext.getSource().getPosition(),
                            new CompoundTag(),
                            true
                    );
                }).then(Commands.argument("pos", Vec3Argument.vec3())
                        .executes((commandContext) -> {
                            return SummonCommandAccessor.invokeSpawnEntity(
                                    commandContext.getSource(),
                                    ResourceArgument.getSummonableEntityType(commandContext, "entity"),
                                    Vec3Argument.getVec3(commandContext, "pos"),
                                    new CompoundTag(),
                                    true
                            );
                        }).then(Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                .executes((commandContext) -> {
                                    return SummonCommandAccessor.invokeSpawnEntity(
                                            commandContext.getSource(),
                                            ResourceArgument.getSummonableEntityType(commandContext, "entity"),
                                            Vec3Argument.getVec3(commandContext, "pos"),
                                            CompoundTagArgument.getCompoundTag(commandContext, "nbt"),
                                            false
                                    );
                                })))));
        ci.cancel();
    }
}
