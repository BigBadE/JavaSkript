package software.bigbade.javaskript.parser;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.api.exception.IllegalScriptException;
import software.bigbade.javaskript.api.factory.LineConverterFactory;
import software.bigbade.javaskript.api.objects.ParsedSkriptMethod;
import software.bigbade.javaskript.api.objects.ParsedSkriptObject;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;
import software.bigbade.javaskript.parser.types.SkriptRegisteredTypes;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ScriptParser implements IScriptParser {
    private final File file;

    private final SkriptLineConverter main = LineConverterFactory.getLineConverter("Main", new String[0], null);
    private final List<SkriptLineConverter> classes = new ArrayList<>();

    private int spaces = 0;

    //Null on top level object
    @Nullable
    private ParsedSkriptObject skriptObject;

    @Override
    public void parse() {
        int lineNumber = 0;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, lineNumber);
                lineNumber++;
            }
            main.endClass();
        } catch (IOException e) {
            SkriptLogger.getLogger().log(Level.SEVERE, "Could not read script", e);
        }
    }

    @Override
    public void parseLine(String line, int lineNumber) {
        if(line.isEmpty()) {
            return;
        }
        checkSpaces(line, lineNumber);
        line = line.toLowerCase().trim();
        if(line.isEmpty() || line.charAt(0) == '#') {
            return;
        }

        if(skriptObject != null && !skriptObject.shouldParseMethods()) {
            skriptObject.parseLine(line);
            return;
        }

        if(line.charAt(line.length()-1) == ':') {
            for (SkriptStructuredObject structuredObject : SkriptRegisteredTypes.getStructuredObjects()) {
                ParsedSkriptObject parsedObject = structuredObject.parse(this, main, line);
                if (parsedObject != null) {
                    parsedObject.setParent(skriptObject);
                    skriptObject = parsedObject;
                    spaces += 4;
                    return;
                }
            }
        }

        if(skriptObject == null) {
            throw new IllegalScriptException("Line " + lineNumber + " isn't inside a method!");
        }

        if(line.startsWith("set ")) {

        }

        if(parseMethod(line, true)) {
            return;
        }

        throw new IllegalScriptException("Unknown expression at line " + lineNumber + ":\n" + line + "\n(Make sure you have the correct version/addons installed)");
    }

    @Override
    public boolean parseMethod(String line, boolean dropUnusedReturns) {
        for(SkriptMethod foundMethod : SkriptRegisteredTypes.getMethods()) {
            ParsedSkriptMethod parsedMethod = foundMethod.parse(this, line);
            if(parsedMethod != null) {
                assert skriptObject != null;
                skriptObject.getConverter().callSkriptMethod(parsedMethod);
                //Prevent stack pollution from non-set variables.
                if(dropUnusedReturns && parsedMethod.getReturnType() != null) {
                    skriptObject.getConverter().dropTopStack();
                }
                return true;
            }
        }
        return false;
    }

    private void checkSpaces(String line, int lineNumber) {
        int change = -spaces;
        int index = 0;
        int current;
        while ((current = ScriptParser.getIncrease(line.charAt(index++))) != -1) {
            change += current;
        }
        if(spaces%4!=0) {
            throw new IllegalScriptException("Invalid amount of spaces at line " + lineNumber);
        }
        for(int i = change/4; i < 0; i++) {
            if(skriptObject == null) {
                throw new IllegalScriptException("Tried to get parent of top class!");
            }
            skriptObject.finishParsing();
            skriptObject = skriptObject.getParent();
        }
    }

    private static int getIncrease(char symbol) {
        switch (symbol) {
            case ' ':
                return 1;
            case '\u0009':
                return 4;
            default:
                return -1;
        }
    }

    public List<SkriptLineConverter> getClasses() {
        return classes;
    }
}
