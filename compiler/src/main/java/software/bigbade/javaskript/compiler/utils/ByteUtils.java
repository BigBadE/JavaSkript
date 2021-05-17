package software.bigbade.javaskript.compiler.utils;

import java.nio.ByteBuffer;

public final class ByteUtils {
    private ByteUtils() {}

    private static final ByteBuffer BYTE_BUFFER = ByteBuffer.allocate(8);

    public static byte[] intToBytes(int value) {
        BYTE_BUFFER.clear();
        BYTE_BUFFER.putInt(value);
        return BYTE_BUFFER.array();
    }
}
