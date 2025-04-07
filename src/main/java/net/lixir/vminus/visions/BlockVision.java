package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.parser.*;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.IBlockVisionAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BlockVision extends Vision {
    public static final BlockVision EMPTY = new BlockVision();

    public final VisionProperty<Integer> light_level;
    public final VisionProperty<Float> speed_boost;
    public final VisionProperty<Float> jump_boost;
    public final VisionProperty<Float> friction;
    public final VisionProperty<Float> blast_resistance;
    public final VisionProperty<Float> break_speed;
    public final VisionProperty<Block> replace;
    public final VisionProperty<Boolean> emissive;
    public final VisionProperty<Boolean> occlude;
    public final VisionProperty<Boolean> redstone_conductor;
    public final VisionProperty<Boolean> ban;
    public final VisionProperty<SoundType> sound;

    public BlockVision() {
        light_level = create("light_level", new VisionIntParser());
        speed_boost = create("speed_boost", new VisionFloatParser());
        jump_boost = create("jump_boost", new VisionFloatParser());
        friction = create("friction", new VisionFloatParser());
        blast_resistance = create("blast_resistance", new VisionFloatParser());
        break_speed = create("break_speed", new VisionFloatParser());
        replace = create("replace", new VisionBlockParser());
        emissive = create("emissive", new VisionBooleanParser());
        occlude = create("occlude", new VisionBooleanParser());
        redstone_conductor = create("redstone_conductor", new VisionBooleanParser());
        ban = create("ban", new VisionBooleanParser());
        sound = create("sound", new VisionSoundTypeParser());

        VisionExtensions.applyExtensions(this);
    }

    public static BlockVision of(Block block) {
        if (block instanceof IBlockVisionAccessor iBlockVisionAccessor)
            return iBlockVisionAccessor.vminus$getVision();
        return EMPTY;
    }
}
