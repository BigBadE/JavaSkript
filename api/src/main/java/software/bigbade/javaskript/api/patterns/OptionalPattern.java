package software.bigbade.javaskript.api.patterns;

import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;

public class OptionalPattern implements PatternType {
    private String word;

    @Override
    public void parseString(String input) {
        word = input;
    }

    @Nullable
    @Override
    public SkriptType<?> getType() {
        return null;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase(word);
    }

    @Override
    public int getTotalSize() {
        return word.length();
    }
}
