package net.lixir.vminus.item.inventory;

import net.lixir.vminus.client.gui.DefaultGuiScreen;
import net.lixir.vminus.registry.VMinusItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class DefaultItemInventoryCapability implements ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(this::createItemHandler);

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemDropped(ItemTossEvent event) {
        if (event.getEntity().getItem().getItem() == VMinusItems.DEFAULT_ITEM.get()) {
            if (Minecraft.getInstance().screen instanceof DefaultGuiScreen) {
                Minecraft.getInstance().player.closeContainer();
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return capability == ForgeCapabilities.ITEM_HANDLER ? this.inventory.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getItemHandler().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getItemHandler().deserializeNBT(nbt);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(3) {
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() != VMinusItems.DEFAULT_ITEM.get();
            }

            @Override
            public void setSize(int size) {
            }
        };
    }

    private ItemStackHandler getItemHandler() {
        return inventory.orElseThrow(RuntimeException::new);
    }
}
