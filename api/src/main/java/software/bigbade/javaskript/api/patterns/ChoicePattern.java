package software.bigbade.javaskript.api.patterns;

import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class ChoicePattern implements PatternType {
    private static final Pattern OPTIONAL_PATTERN = Pattern.compile("\\|");

    private String[] options;

    @Override
    public void parseString(String input) {
        options = OPTIONAL_PATTERN.split(input);
    }

    @Nullable
    @Override
    public SkriptType getType() {
        return null;
    }

    @Override
    public boolean matches(String input) {
        for(String option : options) {
            if(input.equalsIgnoreCase(option)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getTotalSize() {
        return -1;
    }
}
