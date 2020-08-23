package software.bigbade.javaskript.api.patterns;

import software.bigbade.javaskript.api.variables.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternParser {
    private static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

    private final List<PatternType> patterns = new ArrayList<>();

    public PatternParser(String pattern) {
        String[] splitPattern = SPLIT_PATTERN.split(pattern);
        int i = 0;
        while (i < pattern.length()) {
            SubPatternParser patternParser;
            PatternType patternType;
            char character = pattern.charAt(i);
            switch (character) {
                case '[':
                    patternType = new OptionalPattern();
                    patternParser = new SubPatternParser(']');
                    break;
                case '(':
                    patternType = new ChoicePattern();
                    patternParser = new SubPatternParser(')');
                    break;
                case '%':
                    patternType = new VariablePattern();
                    patternParser = new SubPatternParser('%');
                    break;
                default:
                    patternType = new DefaultPattern();
                    patternParser = new SubPatternParser();
            }
            patternType.parseString(patternParser.getFinalString(splitPattern, i));
            patterns.add(patternType);
            i = patternParser.getIndex();
        }
    }

    public Variables parse(String line) {
        String currentWord = line;
        Variables variables = new Variables();
        for (PatternType type : patterns) {
            String word;
            if (type.getTotalSize() == -1) {
                word = currentWord.substring(0, currentWord.indexOf(' '));
            } else {
                word = currentWord.substring(0, type.getTotalSize());
            }
            if (type.matches(word)) {
                if (type.getType() != null) {
                    variables.addVariable(word, type.getType());
                }
                currentWord = currentWord.substring(word.length());
            } else if (!(type instanceof OptionalPattern)) {
                return null;
            }
        }
        return variables;
    }
}
