package software.bigbade.javaskript.api.objects;

public interface SkriptMethod {
    /**
     * Parses the line
     * @param line The line to parse
     * @return Whether the line fits the pattern.
     */
    boolean parse(String line);

    Class<?> getOwner();

    String getMethod();
}
