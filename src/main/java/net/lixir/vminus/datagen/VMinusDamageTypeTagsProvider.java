package net.lixir.vminus.datagen;

import net.lixir.vminus.VMinus;
import net.lixir.vminus.registry.util.VMinusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class VMinusDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public VMinusDamageTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, VMinus.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(VMinusTags.DamageTypes.BLUNT_DAMAGE)
                .add(DamageTypes.CACTUS)
                .add(DamageTypes.CRAMMING)
                .add(DamageTypes.FALLING_ANVIL)
                .add(DamageTypes.FALLING_STALACTITE)
                .add(DamageTypes.FALLING_BLOCK)
                .add(DamageTypes.MOB_ATTACK)
                .add(DamageTypes.MOB_ATTACK_NO_AGGRO)
                .add(DamageTypes.MOB_PROJECTILE)
                .add(DamageTypes.PLAYER_ATTACK)
                .add(DamageTypes.STING)
                .add(DamageTypes.THROWN)
                .add(DamageTypes.TRIDENT)
                .add(DamageTypes.SWEET_BERRY_BUSH)
                .add(DamageTypes.FLY_INTO_WALL)
                .add(DamageTypes.ARROW);
        tag(VMinusTags.DamageTypes.FALL_DAMAGE)
                .add(DamageTypes.STALAGMITE)
                .add(DamageTypes.FALL);
        tag(VMinusTags.DamageTypes.FIRE_DAMAGE)
                .add(DamageTypes.IN_FIRE)
                .add(DamageTypes.LAVA)
                .add(DamageTypes.HOT_FLOOR)
                .add(DamageTypes.ON_FIRE);
        tag(VMinusTags.DamageTypes.MAGIC_DAMAGE)
                .add(DamageTypes.MAGIC)
                .add(DamageTypes.WITHER)
                .add(DamageTypes.INDIRECT_MAGIC)
                .add(DamageTypes.SONIC_BOOM)
                .add(DamageTypes.LIGHTNING_BOLT)
                .add(DamageTypes.OUTSIDE_BORDER)
                .add(DamageTypes.DRAGON_BREATH);
        tag(VMinusTags.DamageTypes.BLAST_DAMAGE)
                .add(DamageTypes.EXPLOSION)
                .add(DamageTypes.PLAYER_EXPLOSION)
                .add(DamageTypes.FIREWORKS)
                .add(DamageTypes.FIREBALL)
                .add(DamageTypes.UNATTRIBUTED_FIREBALL)
                .add(DamageTypes.WITHER_SKULL)
                .add(DamageTypes.BAD_RESPAWN_POINT);

    }
}
