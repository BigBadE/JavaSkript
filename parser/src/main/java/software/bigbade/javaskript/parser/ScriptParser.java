package software.bigbade.javaskript.parser;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.api.exception.IllegalScriptException;
import software.bigbade.javaskript.api.factory.LineConverterFactory;
import software.bigbade.javaskript.api.objects.ParsedSkriptMethod;
import software.bigbade.javaskript.api.objects.ParsedSkriptObject;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;
import software.bigbade.javaskript.parser.types.SkriptRegisteredTypes;

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
public class ScriptParser {
    private final SkriptRegisteredTypes types;
    private final File file;

    private final SkriptLineConverter main = LineConverterFactory.getLineConverter("Main", new String[0], null);
    private final List<SkriptLineConverter> classes = new ArrayList<>();

    private MethodLineConverter method;

    private int spaces = 0;

    private ParsedSkriptObject skriptObject;

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

    private void parseLine(String line, int lineNumber) {
        if(line.isEmpty()) {
            return;
        }
        int change = -spaces;
        int index = 0;
        while (line.charAt(index++) == ' ') {
            change += 1;
        }
        if(spaces%4!=0) {
            throw new IllegalScriptException("Invalid amount of spaces at line " + lineNumber);
        }
        for(int i = change/4; i < 0; i++) {
            skriptObject.finishParsing();
            skriptObject = skriptObject.getParent();
        }
        line = line.trim();
        if(line.isEmpty() || line.charAt(0) == '#') {
            return;
        }

        for(SkriptStructuredObject structuredObject : types.getStructuredObjects()) {
            ParsedSkriptObject parsedObject = structuredObject.parse(main, line);
            if(parsedObject != null) {
                parsedObject.setParent(skriptObject);
                parsedObject.parseLine(line);
                skriptObject = parsedObject;
                spaces += 4;
                return;
            }
        }

        for(SkriptMethod foundMethod : types.getMethods()) {
            ParsedSkriptMethod parsedMethod = foundMethod.parse(line);
            if(parsedMethod != null) {
                method.callMethod(parsedMethod);
                return;
            }
        }

        throw new IllegalScriptException("Unknown line: " + line + " (Make sure you have the correct addons installed)");
    }

    public List<SkriptLineConverter> getClasses() {
        return classes;
    }
}
