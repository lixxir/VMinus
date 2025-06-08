package net.lixir.vminus.audio;

import net.minecraft.client.sounds.AudioStream;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class OpusAudioStream implements AudioStream {
    private final OpusDecoder decoder;
    private final InputStream input;
    private final AudioFormat format;
    private final byte[] pcmBuffer;
    private final byte[] opusBuffer;

    public OpusAudioStream(InputStream stream) throws OpusException {
        this.input = stream;
        this.decoder = new OpusDecoder(48000, 2);
        this.format = new AudioFormat(48000, 16, 2, true, false);
        this.pcmBuffer = new byte[1920 * 2 * 2];
        this.opusBuffer = new byte[4096];
    }

    @Override
    public @NotNull AudioFormat getFormat() {
        return format;
    }

    @Override
    public @NotNull ByteBuffer read(int size) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read;
        while (output.size() < size && (read = input.read(opusBuffer)) != -1) {
            int samples = 0;
            try {
                samples = decoder.decode(opusBuffer, 0, read, pcmBuffer, 0, 960, false);
            } catch (OpusException e) {
                throw new RuntimeException(e);
            }
            output.write(pcmBuffer, 0, samples * 2 * 2);
        }
        return ByteBuffer.wrap(output.toByteArray());
    }

    public ByteBuffer readAll() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read;
        while ((read = input.read(opusBuffer)) != -1) {
            int samples = 0;
            try {
                samples = decoder.decode(opusBuffer, 0, read, pcmBuffer, 0, 960, false);
            } catch (OpusException e) {
                throw new RuntimeException(e);
            }
            output.write(pcmBuffer, 0, samples * 2 * 2);
        }
        return ByteBuffer.wrap(output.toByteArray());
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
}