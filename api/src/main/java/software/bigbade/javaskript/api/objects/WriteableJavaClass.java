package software.bigbade.javaskript.api.objects;

import java.io.IOException;
import java.util.jar.JarOutputStream;

public interface WriteableJavaClass {
    void writeData(JarOutputStream outputStream) throws IOException;
}
