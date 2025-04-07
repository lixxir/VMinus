package net.lixir.vminus.mixins.command;


import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.BlockVision;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(BlockStateArgument.class)
public abstract class BlockStateArgumentMixin {

    @Inject(method = "listSuggestions", at = @At("RETURN"), cancellable = true)
    private <S> void vminus$listSuggestions(CommandContext<S> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        BlockStateArgument self = (BlockStateArgument) (Object) this;
        BlockStateArgumentAccessor accessor = (BlockStateArgumentAccessor) self;
        HolderLookup<Block> blocks = accessor.getBlocks();

        CompletableFuture<Suggestions> suggestions = Suggestions.empty().thenApply(s -> {
            blocks.listElements()
                    .filter(holder -> {
                        Block block = holder.value();
                        BlockVision blockVision = BlockVision.of(block);
                        boolean banned = Boolean.TRUE.equals(blockVision.ban.value(new VisionConditionArguments(block)));
                        boolean hasReplacement = blockVision.replace.value(new VisionConditionArguments(block)) != null;
                        return !banned && !hasReplacement;
                    })
                    .forEach(holder -> builder.suggest(holder.unwrapKey().orElseThrow().location().toString()));
            return builder.build();
        });

        cir.setReturnValue(suggestions);
    }
}