package net.lixir.vminus.mixins.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.lixir.vminus.visions.conditions.VisionConditionArguments;
import net.lixir.vminus.visions.util.VisionItemReplacement;
import net.lixir.vminus.visions.ItemVision;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import java.util.concurrent.CompletableFuture;

@Mixin(ItemArgument.class)
public abstract class ItemArgumentMixin {

    @Inject(method = "listSuggestions", at = @At("RETURN"), cancellable = true)
    private <S> void vminus$listSuggestions(CommandContext<S> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
        ItemArgument self = (ItemArgument) (Object) this;
        ItemArgumentAccessor accessor = (ItemArgumentAccessor) self;
        HolderLookup<Item> items = accessor.getItems();

        CompletableFuture<Suggestions> suggestions = Suggestions.empty().thenApply(s -> {
            items.listElements()
                .filter(holder -> {
                    Item item = holder.value();
                    ItemVision itemVision = ItemVision.of(item);
                    VisionItemReplacement visionItemReplacement = itemVision.replace.value(new VisionConditionArguments(item));
                    Item replacement = visionItemReplacement != null ? (visionItemReplacement.itemStack() != null ? visionItemReplacement.itemStack().getItem() : null) : null;
                    Boolean banned = visionItemReplacement != null ? itemVision.ban.value(new VisionConditionArguments(item)) : null;

                    return (replacement == null) && (banned == null || !banned);
                })
                .forEach(holder -> builder.suggest(holder.unwrapKey().orElseThrow().location().toString()));

            return builder.build();
        });

        cir.setReturnValue(suggestions);
    }
}
