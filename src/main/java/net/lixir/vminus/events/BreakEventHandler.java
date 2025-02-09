package net.lixir.vminus.events;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.traits.Trait;
import net.lixir.vminus.traits.Traits;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BreakEventHandler {
    @SubscribeEvent
    public static void breakEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getMainHandItem();
        LevelAccessor level = event.getLevel();
        BlockState blockState = event.getState();
        BlockPos blockPos = event.getPos();
        Block block = blockState.getBlock();

        for (Trait trait : Traits.getTraits(mainHand)) {
            VMinus.LOGGER.info("break event trait: " + trait);
            if (trait.onMine(mainHand, player, level, blockState, blockPos, block))
               if (event.isCancelable())
                   event.setCanceled(true);
        }

    }
}
