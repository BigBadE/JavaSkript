package software.bigbade.javaskript.compiler.utils;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.io.DataOutput;
import java.io.IOException;
import java.util.jar.JarOutputStream;

@RequiredArgsConstructor
public class JarDataStream implements DataOutput {
    private final JarOutputStream outputStream;

    @Override
    public void write(int b) throws IOException {
        outputStream.write(ByteUtils.intToBytes(b));
    }

    @Override
    public void write(@Nonnull byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(@Nonnull byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeByte(int v) throws IOException {
        outputStream.write(ByteUtils.intToBytes(v));
    }

    @Override
    public void writeShort(int v) throws IOException {
        outputStream.write(ByteUtils.intToBytes(v));
    }

    @Override
    public void writeChar(int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeInt(int v) throws IOException {
        outputStream.write(ByteUtils.intToBytes(v));
    }

    @Override
    public void writeLong(long v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFloat(float v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeDouble(double v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(@Nonnull String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(@Nonnull String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeUTF(@Nonnull String s) {
        throw new UnsupportedOperationException();
    }
}
