package net.lixir.vminus.vision;

import net.lixir.vminus.vision.resource.codec.VisionCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class VisionPropertyType<T> {
    private final String id;
    private final VisionCodec<T> codec;
    private final boolean syncToClient;

    public VisionPropertyType(String id, VisionCodec<T> codec, boolean syncToClient) {
        this.id = id;
        this.codec = codec;
        this.syncToClient = syncToClient;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <T> @NotNull VisionPropertyType<T> create(String id, VisionCodec<T> codec) {
        return new VisionPropertyType<>(id, codec, true);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> @NotNull VisionPropertyType<T> create(String id, VisionCodec<T> codec, boolean syncToClient) {
        return new VisionPropertyType<>(id, codec, syncToClient);
    }

    public String getId() {
        return id;
    }

    public VisionCodec<T> getCodec() {
        return codec;
    }

    public boolean shouldSyncToClient() {
        return syncToClient;
    }
}

