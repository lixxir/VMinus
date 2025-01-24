package net.lixir.vminus.mixins.items;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemPredicate.Builder.class)
public class ItemPredicateBuilderMixin {
    /*
    @ModifyVariable(
            method = "<init>",
            at = @At(value = "INVOKE", target = ""),
            ordinal = 0
    )
    private static LootItemCondition.Builder detour$hasShears(LootItemCondition.Builder original) {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES));
    }

     */
}
