package net.lixir.vminus.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionDuck;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class VisionCommand {

    @SubscribeEvent
    public static void registerCommand(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(
            Commands.literal("vision")
                    .then(Commands.argument("type", StringArgumentType.string())
                    .suggests(VISION_TYPE_PROVIDER)
                    .then(Commands.argument("feature", ResourceLocationArgument.id())
                        .suggests(FEATURE_PROVIDER)
                        .executes(VisionCommand::execute)
                    )
                )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        String visionTypeId = StringArgumentType.getString(ctx, "type");
        VisionType<?> visionType = VisionTypes.get(visionTypeId);

        if (visionType == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown VisionType: " + visionTypeId));
            return 0;
        }

        ResourceLocation featureId = ResourceLocationArgument.getId(ctx, "feature");
        Registry<?> registry = visionType.getRegistry();
        Object feature = registry.getOptional(featureId).orElse(null);

        if (feature == null) {
            ctx.getSource().sendFailure(Component.literal("Unknown feature: " + featureId + " in registry " + registry.key().location()));
            return 0;
        }

        VisionDuck duck = (VisionDuck) feature;
        Vision vision = Vision.getVision(visionType, duck.vMinus$getVisionId());
        CompoundTag nbt = vision.toNbt();
        Component component = NbtUtils.toPrettyComponent(nbt);
        ctx.getSource().sendSuccess(() -> Component.literal("Vision[" + featureId + "]=").append(component), false);
        return 1;
    }


    private static final SuggestionProvider<CommandSourceStack> VISION_TYPE_PROVIDER = (ctx, builder) -> {
        for (VisionType<?> type : VisionTypes.getAll()) {
            builder.suggest(type.getId());
        }
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> FEATURE_PROVIDER = (ctx, builder) -> {
        String visionTypeId = StringArgumentType.getString(ctx, "type");
        VisionType<?> visionType = VisionTypes.get(visionTypeId);

        if (visionType != null) {
            Registry<?> registry = visionType.getRegistry();
            registry.keySet().forEach(id -> builder.suggest(id.toString()));
        }

        return builder.buildFuture();
    };
}
