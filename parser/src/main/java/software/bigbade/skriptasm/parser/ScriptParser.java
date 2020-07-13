package software.bigbade.skriptasm.parser;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.SkriptLineConverter;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.api.exception.IllegalScriptException;
import software.bigbade.javaskript.api.objects.SkriptObject;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.skriptasm.parser.types.SkriptRegisteredTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ScriptParser {
    private final File file;
    private final SkriptRegisteredTypes types;
    private final SkriptLineConverter lineConverter;

    private int spaces = 0;

    private SkriptObject skriptObject;

    public void parse() {
        int lineNumber = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(line, lineNumber);
                lineNumber++;
            }
        } catch (IOException e) {
            SkriptLogger.getLogger().log(Level.SEVERE, "Could not read script", e);
        }
    }

    private void parseLine(String line, int lineNumber) {
        int change = -spaces;
        int index = 0;
        while (line.charAt(index++) == ' ') {
            change += 1;
        }
        if(spaces%4!=0) {
            throw new IllegalScriptException("Invalid amount of spaces at line " + lineNumber);
        }
        for(int i = change/4; i < 0; i++) {
            skriptObject = skriptObject.getParent();
        }
        
        for(SkriptStructuredObject structuredObject : types.getStructuredObjects()) {
            if(structuredObject.parse(line)) {
                skriptObject = new SkriptObject(skriptObject, structuredObject);
                structuredObject.register(lineConverter, line);
                spaces += 4;
                return;
            }
        }

        for(SkriptMethod method : types.getMethods()) {
            if(method.parse(line)) {
                lineConverter.callMethod(method, line);
                return;
            }
        }

        throw new IllegalScriptException("Unknown method: " + line + " (Make sure you have the correct addons installed)");
    }
}
