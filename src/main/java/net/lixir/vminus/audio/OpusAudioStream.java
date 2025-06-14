package net.lixir.vminus.audio;

import net.minecraft.client.sounds.AudioStream;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class OpusAudioStream implements AudioStream {
    private static final int SAMPLE_RATE = 48000;
    private static final int CHANNELS = 2;
    private static final int FRAME_SIZE = 960;
    private static final int MAX_FRAME_BYTES = 4000;

    private final InputStream input;
    private final AudioFormat format;
    private final OpusDecoder decoder;
    private final short[] decodeBuffer;
    private final ByteBuffer outputBuffer;

    private boolean closed = false;
    private boolean endOfStream = false;

    public OpusAudioStream(InputStream opusStream) throws IOException {
        this.input = opusStream;
        this.format = new AudioFormat(SAMPLE_RATE, 16, CHANNELS, true, false);
        try {
            this.decoder = new OpusDecoder(SAMPLE_RATE, CHANNELS);
        } catch (OpusException e) {
            throw new IOException("Failed to initialize Opus decoder", e);
        }

        this.decodeBuffer = new short[FRAME_SIZE * CHANNELS];
        this.outputBuffer = ByteBuffer.allocate(8192);
        this.outputBuffer.flip();
    }

    @Override
    public @NotNull AudioFormat getFormat() {
        return format;
    }

    @Override
    public @NotNull ByteBuffer read(int size) throws IOException {
        if (closed) throw new IOException("Stream closed");

        ByteBuffer result = ByteBuffer.allocate(size);
        while (result.position() < size && !endOfStream) {
            if (!outputBuffer.hasRemaining()) {
                refillOutputBuffer();
            }

            int toCopy = Math.min(outputBuffer.remaining(), size - result.position());
            byte[] temp = new byte[toCopy];
            outputBuffer.get(temp);
            result.put(temp);
        }

        result.flip();
        return result;
    }

    private void refillOutputBuffer() throws IOException {
        byte[] frameBytes = new byte[MAX_FRAME_BYTES];
        int bytesRead = input.read(frameBytes);
        if (bytesRead == -1) {
            endOfStream = true;
            return;
        }

        int decodedSamples;
        try {
            decodedSamples = decoder.decode(frameBytes, 0, bytesRead, decodeBuffer, 0, FRAME_SIZE, false);
        } catch (OpusException e) {
            throw new IOException("Error decoding Opus frame", e);
        }

        outputBuffer.clear();
        for (int i = 0; i < decodedSamples * CHANNELS; i++) {
            short sample = decodeBuffer[i];
            outputBuffer.put((byte) (sample & 0xFF));
            outputBuffer.put((byte) ((sample >> 8) & 0xFF));
        }
        outputBuffer.flip();
    }

    public ByteBuffer readAll() throws IOException {
        ByteBuffer result = ByteBuffer.allocate(16384);
        ByteBuffer chunk;
        while ((chunk = read(2048)).hasRemaining()) {
            if (result.remaining() < chunk.remaining()) {
                ByteBuffer bigger = ByteBuffer.allocate(result.capacity() * 2);
                result.flip();
                bigger.put(result);
                result = bigger;
            }
            result.put(chunk);
        }
        result.flip();
        return result;
    }

    @Override
    public void close() {
        closed = true;
        try {
            input.close();
        } catch (IOException ignored) {}
    }
}
