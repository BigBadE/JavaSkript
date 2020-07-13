package software.bigbade.javaskript.compiler.utils;

import software.bigbade.javaskript.api.SkriptLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public final class MD5Checksum {
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

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getMD5Checksum(File file) {
        byte[] b = createChecksum(file);
        StringBuilder result = new StringBuilder();

        for (byte value : b) {
            result.append(Integer.toString((value & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}