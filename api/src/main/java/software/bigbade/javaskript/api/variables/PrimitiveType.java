package software.bigbade.javaskript.api.variables;

public abstract class PrimitiveType implements SkriptType {
    @Override
    public boolean isSerializable() {
        return true;
    }

    @Override
    public boolean isSet() {
        return true;
    }
}
