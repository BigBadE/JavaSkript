package software.bigbade.javaskript.parser.utils;

import software.bigbade.javaskript.api.SkriptLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public final class MD5Checksum {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
    private MD5Checksum() {}

    public static byte[] createChecksum(File file) {
        try(InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            return complete.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            SkriptLogger.getLogger().log(Level.SEVERE, "Could not generate hash for skript", e);
        }
        return new byte[0];
    }
    public static String getMD5Checksum(File file) {
        byte[] bytes = createChecksum(file);
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
