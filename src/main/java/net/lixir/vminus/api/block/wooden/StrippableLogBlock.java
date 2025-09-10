package net.lixir.vminus.api.block.wooden;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrippableLogBlock extends LogBlock {
    private final Supplier<Block> stripped;

    public StrippableLogBlock(Properties properties, Supplier<Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
            return getStripped().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    public Block getStripped() {
        return stripped.get();
    }
}