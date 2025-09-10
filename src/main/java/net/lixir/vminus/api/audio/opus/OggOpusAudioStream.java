package net.lixir.vminus.api.audio.opus;

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
import java.nio.charset.StandardCharsets;

/**
 * An {@link AudioStream} implementation for reading Ogg Opus audio from an {@link InputStream}.
 * <p>
 * This class decodes Ogg Opus packets on the fly using the Concentus Opus decoder
 * and provides the audio in PCM16 format.
 */
public class OggOpusAudioStream implements AudioStream {
    private static final int SAMPLE_RATE = 48000;
    private static final int FRAME_SIZE = 960; // 20ms at 48kHz

    private AudioFormat format;
    private OpusDecoder decoder;
    private short[] decodeBuffer;
    private final ByteBuffer outputBuffer;
    private final OggPacketReader reader;

    private boolean closed = false;
    private final InputStream sourceStream;
    private boolean initialized = false;
    private int channels = 1;

    /**
     * Constructs a new OggOpusAudioStream from the given input stream.
     *
     * @param stream the input stream containing Ogg Opus audio data
     * @throws IOException if the stream cannot be read
     */
    public OggOpusAudioStream(InputStream stream) throws IOException {
        this.sourceStream = stream;
        this.reader = new OggPacketReader(stream);
        this.outputBuffer = ByteBuffer.allocateDirect(16384);
        this.outputBuffer.flip();
    }

    /**
     * Returns the {@link AudioFormat} of the decoded audio.
     * Defaults to mono 48kHz PCM16 if not yet initialized.
     *
     * @return the audio format
     */
    @Override
    public @NotNull AudioFormat getFormat() {
        if (format == null)
            return new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        return format;
    }

    /**
     * Reads the entire remaining audio stream into a direct {@link ByteBuffer}.
     *
     * @return a ByteBuffer containing all decoded audio
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Reads up to {@code size} bytes of decoded audio into a direct {@link ByteBuffer}.
     *
     * @param size the maximum number of bytes to read
     * @return a ByteBuffer containing up to {@code size} bytes of audio
     * @throws IOException if the stream is closed or a decoding error occurs
     */
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

    /**
     * Decodes the next Ogg Opus packet into the internal output buffer.
     *
     * @return true if a packet was successfully decoded, false if the stream ended
     * @throws IOException if a decoding error occurs
     */
    private boolean decodeNextPacket() throws IOException {
        while (true) {
            OggPacket packet = reader.getNextPacket();
            if (packet == null)
                return false;

            byte[] data = packet.getData();

            // Parse OpusHead to initialize decoder and channel count
            if (data.length >= 19 && new String(data, 0, 8, StandardCharsets.US_ASCII).equals("OpusHead")) {
                channels = data[9] & 0xFF; // unsigned byte
                format = new AudioFormat(SAMPLE_RATE, 16, channels, true, false);
                try {
                    decoder = new OpusDecoder(SAMPLE_RATE, channels);
                } catch (OpusException e) {
                    throw new IOException("Failed to init Opus decoder", e);
                }
                decodeBuffer = new short[FRAME_SIZE * channels];
                initialized = true;
                continue;
            }

            // Skip OpusTags packets
            if (data.length >= 8 && new String(data, 0, 8, StandardCharsets.US_ASCII).equals("OpusTags"))
                continue;


            if (!initialized)
                continue;

            int samples;
            try {
                samples = decoder.decode(data, 0, data.length, decodeBuffer, 0, FRAME_SIZE, false);
            } catch (OpusException e) {
                throw new IOException("Opus decode error", e);
            }

            outputBuffer.clear();
            for (int i = 0; i < samples * channels; i++) {
                short sample = decodeBuffer[i];
                outputBuffer.put((byte) (sample & 0xFF));
                outputBuffer.put((byte) ((sample >> 8) & 0xFF));
            }
            outputBuffer.flip();

            return true;
        }
    }

    /**
     * Closes the audio stream and the underlying {@link InputStream}.
     * Logs a warning if closing the stream fails.
     */
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
