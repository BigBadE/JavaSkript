package software.bigbade.javaskript.api.patterns;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubPatternParser {
    private final Character escape;
    private final StringBuilder finalString = new StringBuilder();
    @Getter
    private int index;

    public SubPatternParser(char escape) {
        this.escape = escape;
    }

    public SubPatternParser() {
        escape = null;
    }

    public String getFinalString(String[] lines, int index) {
        if(escape == null) {
            finalString.append(lines[index]).append(" ");
        } else {
            finalString.append(lines[index].substring(1)).append(" ");
        }
        index++;
        while (true) {
            String word = lines[index++];
            if(escape == null) {
                char character = word.charAt(0);
                if(character == '%' || character == '[' || character == '(') {
                    this.index = index;
                    return finalString.toString();
                }
            } else if(word.charAt(word.length()-1) == escape) {
                this.index = ++index;
                return finalString.substring(0, finalString.length()-1);
            }
            finalString.append(word);
            if(index > lines.length) {
                if(escape == null) {
                    this.index = index;
                    return finalString.toString();
                } else {
                    throw new IllegalArgumentException("Pattern has a special character with no end");
                }
            }
        }
    }
}
