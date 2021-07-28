package com.bigbade.javaskript.api.skript.pattern;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ParseResult {
    /**
     * Contains a map of all the start and stop points of variables in the line passed.
     */
    private final Map<Integer, Integer> variables;
    /**
     * The result of the parsing, failed if it doesnt match, passed if it does, undetermined if it needs more data,
     * and ignored if an optional fails signaling the parser to go back to the start of that segment.
     */
    private final Result result;

    /**
     * Contains a list of all parts found by this part, not including itself.
     */
    private final List<IPatternPart> foundParts;

    public ParseResult(Map<Integer, Integer> variables, List<IPatternPart> foundParts, Result result) {
        this.variables = variables;
        this.foundParts = foundParts;
        this.result = result;
    }

    public ParseResult(IPatternPart foundPart, Result result) {
        this.variables = Collections.emptyMap();
        this.foundParts = Collections.singletonList(foundPart);
        this.result = result;
    }

    public ParseResult(Result result) {
        this.variables = Collections.emptyMap();
        this.foundParts = Collections.emptyList();
        this.result = result;
    }

    /**
     * Builds a parse returner, allowing variables to gradually be added
     */
    public static class ParseResultBuilder {
        private final Map<Integer, Integer> variables = new HashMap<>();
        private final List<IPatternPart> parts = new ArrayList<>();

        public void addVariable(int start, int end) {
            variables.put(start, end);
        }

        public void addPart(IPatternPart part) { parts.add(part); }

        public void addParts(List<IPatternPart> parts) { this.parts.addAll(parts); }

        public ParseResult build(Result result) {
            return new ParseResult(variables, parts, result);
        }
    }

    public enum Result {
        FAILED,
        PASSED,
        IGNORED,
        UNDETERMINED
    }
}
