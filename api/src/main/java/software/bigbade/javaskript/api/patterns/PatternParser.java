package software.bigbade.javaskript.api.patterns;

import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.exception.IllegalScriptException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PatternParser {
    public static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

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

    public boolean parse(IScriptParser parser, String line) {
        String currentWord = line;
        int i = 0;
        List<String> foundVariables = new ArrayList<>();
        while (i < patterns.size()) {
            PatternType type = patterns.get(i);
            if(type instanceof VariablePattern) {
                StringBuilder found = new StringBuilder();
                while ((i == patterns.size()-1) ? !currentWord.isEmpty() : !patterns.get(i++).matches(currentWord)) {
                    String word = currentWord.substring(0, currentWord.indexOf(' '));
                    found.append(word);
                    currentWord = currentWord.substring(word.length());
                }
                foundVariables.add(found.toString());
            }
            int size = (type.getTotalSize() == -1) ? currentWord.indexOf(' ') : type.getTotalSize();
            if(type.matches(currentWord.substring(0, size))) {
                currentWord = currentWord.substring(size);
            } else {
                return false;
            }
            i++;
        }
        for(String variable : foundVariables) {
            if(!parser.parseMethod(variable, false)) {
                throw new IllegalScriptException("Unknown expression: " + variable);
            }
        }
        return true;
    }
}
