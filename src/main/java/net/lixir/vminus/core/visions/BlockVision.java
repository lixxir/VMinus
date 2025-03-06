package net.lixir.vminus.core.visions;

import net.lixir.vminus.core.values.VisionProperty;
import net.lixir.vminus.core.values.BasicVisionValue;
import net.minecraft.world.level.block.SoundType;

public class BlockVision extends Vision<BlockVision> {
    public final VisionProperty<BasicVisionValue<Integer>, Integer> lightLevel = new VisionProperty<>("light_level");

    public final VisionProperty<BasicVisionValue<Float>, Float> speedFactor = new VisionProperty<>("speed_factor");
    public final VisionProperty<BasicVisionValue<Float>, Float> jumpFactor = new VisionProperty<>("jump_factor");
    public final VisionProperty<BasicVisionValue<Float>, Float> friction = new VisionProperty<>("friction");
    public final VisionProperty<BasicVisionValue<Float>, Float> explosionResistance = new VisionProperty<>("explosion_resistance");
    public final VisionProperty<BasicVisionValue<Float>, Float> destroySpeed = new VisionProperty<>("destroy_speed");

    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> emissive = new VisionProperty<>("emissive");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> occludes = new VisionProperty<>("occludes");
    public final VisionProperty<BasicVisionValue<Boolean>, Boolean> redstoneConductor = new VisionProperty<>("redstone_conductor");

    public final VisionProperty<BasicVisionValue<SoundType>, SoundType> sound = new VisionProperty<>("sound");

    @Override
    public void merge(BlockVision vision) {
        lightLevel.merge(vision.lightLevel);
        speedFactor.merge(vision.speedFactor);
        jumpFactor.merge(vision.jumpFactor);
        friction.merge(vision.friction);
        explosionResistance.merge(vision.explosionResistance);
        sound.merge(vision.sound);
        destroySpeed.merge(vision.destroySpeed);
        emissive.merge(vision.emissive);
        occludes.merge(vision.occludes);
        redstoneConductor.merge(vision.redstoneConductor);
    }
}
