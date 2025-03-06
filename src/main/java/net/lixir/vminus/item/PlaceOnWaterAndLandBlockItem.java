package net.lixir.vminus.item;

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
    private final Supplier<Block> blockSupplier;

    public PlaceOnWaterAndLandBlockItem(Supplier<Block> blockSupplier, Item.Properties properties) {
        super(null, properties);
        this.blockSupplier = blockSupplier;
    }

    @Override
    public Block getBlock() {
        return blockSupplier.get();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos targetPos = context.getClickedPos().relative(context.getClickedFace());
        FluidState fluidState = level.getFluidState(targetPos);
        if (!fluidState.isEmpty()) {
            return InteractionResult.PASS;
        }

        InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
        if (!interactionResult.consumesAction() && this.isEdible()) {
            InteractionResult interactionResult1 = this.use(level, context.getPlayer(), context.getHand()).getResult();
            return interactionResult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionResult1;
        } else {
            return interactionResult;
        }
    }
}
