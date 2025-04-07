package net.lixir.vminus.mixins.loottable;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MatchTool.class)
public interface MatchToolAccessor {
    @Accessor("predicate")
    ItemPredicate getPredicate();
}