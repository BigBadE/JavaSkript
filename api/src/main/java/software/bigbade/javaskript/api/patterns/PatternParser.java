package software.bigbade.javaskript.api.patterns;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.api.variables.SkriptType;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PatternParser {
    private static final Pattern choicePattern = Pattern.compile("\\|");
    private static final Pattern spacePattern = Pattern.compile(" ");

    private final String pattern;

    //Types used in the initialization of the method
    private final List<SkriptType<?>> structureTypes;
    private final Variables variables = new Variables();

    private int lineIndex;
    private int currentType;

    private StringBuilder builder = null;

    public Variables parse(String line) {
        lineIndex = currentType = 0;
        for(int i = 0; i < line.length(); i++) {
            char current = pattern.charAt(i);
            switch (current) {
                case '(':
                case '[':
                case '%':
                    if(!checkSpecialCharacters(line, current)) {
                        return null;
                    }
                    break;
                default:
                    if(current != line.charAt(i)) {
                        return null;
                    }
            }
        }
        return variables;
    }

    private boolean checkSpecialCharacters(String line, char current) {
        if(builder != null) {
            if(builder.charAt(0) != builder.charAt(builder.length()-1)) {
                throw new IllegalArgumentException("Pattern has mismatching special characters");
            }

            String found = builder.substring(1, builder.length()-1);
            int added = parseSpecialCharacters(found, line, current);
            if(added == -1) {
                return false;
            }
            lineIndex += added;
        } else {
            builder = new StringBuilder().append(current);
            lineIndex++;
        }
        return true;
    }

    private int parseSpecialCharacters(String found, String line, char current) {
        String currentWord = spacePattern.split(line.substring(lineIndex), 2)[0];
        switch (current) {
            case '(':
                for(String choice : choicePattern.split(found)) {
                    if(choice.equals(currentWord)) {
                        break;
                    }
                }
                return -1;
            case '[':
                if(!found.equals(currentWord)) {
                    currentWord = "";
                }
                break;
            case '%':
                SkriptType<?> type = structureTypes.get(currentType);
                variables.addVariable(type);
                currentType++;
                break;
            default:
                throw new IllegalStateException("Current character swapped between methods");
        }
        return currentWord.length();
    }
}
