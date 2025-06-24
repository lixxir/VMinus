package net.lixir.vminus.audio;

import net.lixir.vminus.VMinus;
import net.minecraft.client.sounds.AudioStream;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;
import org.gagravarr.ogg.OggPacket;
import org.gagravarr.ogg.OggPacketReader;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class OggOpusAudioStream implements AudioStream {
    private static final int SAMPLE_RATE = 48000;
    private static final int CHANNELS = 2;
    private static final int FRAME_SIZE = 960;

    private final AudioFormat format;
    private final OpusDecoder decoder;
    private final short[] decodeBuffer;
    private final ByteBuffer outputBuffer;
    private final OggPacketReader reader;

    private boolean closed = false;
    private final InputStream sourceStream;

    public OggOpusAudioStream(InputStream stream) throws IOException {
        this.sourceStream = stream;
        this.format = new AudioFormat(SAMPLE_RATE, 16, CHANNELS, true, false);
        try {
            this.decoder = new OpusDecoder(SAMPLE_RATE, CHANNELS);
        } catch (OpusException e) {
            throw new IOException("Failed to init Opus decoder", e);
        }

        this.reader = new OggPacketReader(stream);
        this.decodeBuffer = new short[FRAME_SIZE * CHANNELS];
        this.outputBuffer = ByteBuffer.allocateDirect(16384);
        this.outputBuffer.flip();
    }

    @Override
    public @NotNull AudioFormat getFormat() {
        return format;
    }

    public ByteBuffer readAll() throws IOException {
        ByteBuffer all = ByteBuffer.allocateDirect(1024 * 1024);
        ByteBuffer chunk;

        while ((chunk = read(4096)).hasRemaining()) {
            if (all.remaining() < chunk.remaining()) {
                ByteBuffer newBuf = ByteBuffer.allocateDirect(all.capacity() + 1024 * 512);
                all.flip();
                newBuf.put(all);
                all = newBuf;
            }
            all.put(chunk);
        }

        all.flip();
        return all;
    }

    @Override
    public @NotNull ByteBuffer read(int size) throws IOException {
        if (closed)
            throw new IOException("Stream closed");

        ByteBuffer result = ByteBuffer.allocateDirect(size);
        while (result.position() < size) {
            if (!outputBuffer.hasRemaining()) {
                if (!decodeNextPacket())
                    break;
            }

            int toCopy = Math.min(outputBuffer.remaining(), size - result.position());
            ByteBuffer slice = outputBuffer.slice();
            slice.limit(toCopy);
            result.put(slice);
            outputBuffer.position(outputBuffer.position() + toCopy);
        }

        result.flip();
        return result;
    }

    private boolean decodeNextPacket() throws IOException {
        while (true) {
            OggPacket packet = reader.getNextPacket();
            if (packet == null)
                return false;

            byte[] data = packet.getData();

            if (data.length >= 8) {
                String header = new String(data, 0, 8, java.nio.charset.StandardCharsets.US_ASCII);
                if (header.equals("OpusHead") || header.equals("OpusTags")) { // Skip header packets.
                    continue;
                }
            }

            int samples;
            try {
                samples = decoder.decode(data, 0, data.length, decodeBuffer, 0, FRAME_SIZE, false);
            } catch (OpusException e) {
                throw new IOException("Opus decode error", e);
            }

            outputBuffer.clear();
            for (int i = 0; i < samples * CHANNELS; i++) {
                short sample = decodeBuffer[i];
                outputBuffer.put((byte) (sample & 0xFF));
                outputBuffer.put((byte) ((sample >> 8) & 0xFF));
            }
            outputBuffer.flip();

            return true;
        }
    }

    @Override
    public void close() {
        closed = true;
        try {
            sourceStream.close();
        } catch (IOException e) {
            VMinus.LOGGER.warn("Failed to close underlying Opus stream", e);
        }
    }
}
