package net.lixir.vminus.item.trait;

import net.lixir.vminus.VMinus;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class ItemTrait {
    public static ItemTrait DEFAULT_INSTANCE = new ItemTrait(new ResourceLocation(VMinus.ID, "default"), true);
    private final ResourceLocation resourceLocation;
    private final boolean hidden;

    public ItemTrait(ResourceLocation resourceLocation, boolean hidden) {
        this.resourceLocation = resourceLocation;
        this.hidden = hidden;
    }

    public String getName() {
        return resourceLocation.getPath();
    }

    public String getNamespace() {
        return resourceLocation.getNamespace();
    }

    public boolean helmetTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public boolean chestplateTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public boolean leggingsTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public boolean bootsTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public boolean armorTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public boolean onMine(ItemStack mainHand, Player player, LevelAccessor level, BlockState blockState, BlockPos blockPos, Block block) {
        return false;
    }

    public boolean onJump(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {
        return false;
    }

    public ResourceLocation resourceLocation() {
        return resourceLocation;
    }

    public boolean isHidden() {
        return hidden;
    }
}
