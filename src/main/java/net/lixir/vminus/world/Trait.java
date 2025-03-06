package net.lixir.vminus.world;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class Trait {
    private final ResourceLocation resourceLocation;
    private final boolean hidden;

    public Trait(ResourceLocation resourceLocation, boolean hidden) {
        this.resourceLocation = resourceLocation;
        this.hidden = hidden;
    }

    public String getName() {
        return resourceLocation.getPath();
    }

    public String getNamespace() {
        return resourceLocation.getNamespace();
    }

    public boolean helmetTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}

    public boolean chestplateTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}

    public boolean leggingsTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}

    public boolean bootsTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}

    public boolean armorTick(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}

    public boolean onMine(ItemStack mainHand, Player player, LevelAccessor level, BlockState blockState, BlockPos blockPos, Block block) {return  false;}

    public boolean onJump(ItemStack armorStack, LivingEntity entity, LevelAccessor world) {return false;}


    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public boolean isHidden() {
        return hidden;
    }
}
