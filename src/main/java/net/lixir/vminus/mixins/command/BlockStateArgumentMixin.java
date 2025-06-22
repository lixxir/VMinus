package net.lixir.vminus.mixins.command;


import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockStateArgument.class)
public abstract class BlockStateArgumentMixin {

    /*
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
                        boolean banned = Boolean.TRUE.equals(blockVision.ban.value(new VisionContext(block)));
                        boolean hasReplacement = blockVision.replace.value(new VisionContext(block)) != null;
                        return !banned && !hasReplacement;
                    })
                    .forEach(holder -> builder.suggest(holder.unwrapKey().orElseThrow().location().toString()));
            return builder.build();
        });

        cir.setReturnValue(suggestions);
    }

     */
}