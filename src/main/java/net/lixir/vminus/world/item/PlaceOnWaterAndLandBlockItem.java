package net.lixir.vminus.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Supplier;

public class PlaceOnWaterAndLandBlockItem extends PlaceOnWaterBlockItem {

    public PlaceOnWaterAndLandBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos targetPos = context.getClickedPos().relative(context.getClickedFace());
        FluidState fluidState = level.getFluidState(targetPos);
        if (!fluidState.isEmpty())
            return InteractionResult.PASS;

        InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
        if (!interactionResult.consumesAction() && this.isEdible()) {
            InteractionResult interactionResult1 = this.use(level, context.getPlayer(), context.getHand()).getResult();
            return interactionResult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult1;
        } else {
            return interactionResult;
        }
    }
}
