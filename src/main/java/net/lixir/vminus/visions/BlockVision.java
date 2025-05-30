package net.lixir.vminus.visions;

import net.lixir.vminus.visions.resources.VisionCodecs;
import net.lixir.vminus.visions.resources.codec.*;
import net.lixir.vminus.visions.util.VisionType;
import net.lixir.vminus.visions.values.VisionProperty;
import net.lixir.vminus.visions.accessors.BlockVisionAccessor;
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
        light_level = create("light_level", VisionCodecs.intCodec());
        speed_boost = create("speed_boost", VisionCodecs.floatCodec());
        jump_boost = create("jump_boost", VisionCodecs.floatCodec());
        friction = create("friction", VisionCodecs.floatCodec());
        blast_resistance = create("blast_resistance", VisionCodecs.floatCodec());
        break_speed = create("break_speed", VisionCodecs.floatCodec());
        replace = create("replace", new VisionBlockCodec());
        emissive = create("emissive", VisionCodecs.booleanCodec());
        occlude = create("occlude", VisionCodecs.booleanCodec());
        redstone_conductor = create("redstone_conductor", VisionCodecs.booleanCodec());
        ban = create("ban", VisionCodecs.booleanCodec());
        sound = create("sound", new VisionSoundTypeCodec());

        VisionExtensions.applyExtensions(this);
    }

    public static BlockVision of(Block block) {
        if (block instanceof BlockVisionAccessor blockVisionAccessor)
            return blockVisionAccessor.vminus$getVision();
        return EMPTY;
    }

    @Override
    public String getEntryListName() {
        return VisionType.BLOCK.getListName();
    }
}
