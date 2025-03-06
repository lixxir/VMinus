package net.lixir.vminus.block;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class StrippableFlammablePillarBlock extends FlammableRotatedPillarBlock {

    private final RegistryObject<Block> strippedRegistryObject;

    public StrippableFlammablePillarBlock(Properties pProperties, RegistryObject<Block> strippedRegistryObject) {
        super(pProperties);
        this.strippedRegistryObject = strippedRegistryObject;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
            return strippedRegistryObject.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    public RegistryObject<Block> getStrippedRegistryObject() {
        return strippedRegistryObject;
    }
}