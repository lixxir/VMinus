package net.lixir.vminus.mixins.items;

import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeadItem.class)
public class LeadItemMixin {
    @Redirect(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean detour$newTag(BlockState instance, TagKey<Block> tagKey) {
        return instance.is(VMinusTags.Blocks.LEASHABLE);
    }
}
