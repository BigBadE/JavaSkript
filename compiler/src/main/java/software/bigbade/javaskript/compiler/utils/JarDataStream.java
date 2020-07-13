package software.bigbade.javaskript.compiler.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

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
    public void write(@NotNull byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeByte(int v) {
        throw new UnsupportedOperationException();
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
    public void writeBytes(@NotNull String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(@NotNull String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeUTF(@NotNull String s) {
        throw new UnsupportedOperationException();
    }
}
