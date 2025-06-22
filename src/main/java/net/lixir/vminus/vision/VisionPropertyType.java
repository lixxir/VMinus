package net.lixir.vminus.vision;

import net.lixir.vminus.vision.resource.codec.VisionCodec;
import org.jetbrains.annotations.NotNull;


public class VisionPropertyType<T> {
    private final String id;
    private final VisionCodec<T> codec;

    private VisionPropertyType(@NotNull String id, @NotNull VisionCodec<T> codec) {
        this.id = id;
        this.codec = codec;
    }

    public static <T> @NotNull VisionPropertyType<T> create(@NotNull String id, @NotNull VisionCodec<T> codec){
        return new VisionPropertyType<T>(id, codec);
    }

    public VisionCodec<T> getCodec() {
        return codec;
    }

    public String getId() {
        return id;
    }
}
