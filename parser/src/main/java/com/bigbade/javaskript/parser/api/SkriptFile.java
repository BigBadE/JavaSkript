package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SkriptFile implements ISkriptFile {
    @Getter
    private final String fileName;

    @Getter
    private final List<IParsingDef> parsedFunctions = new ArrayList<>();

    @Override
    public void addParsedFunction(IParsingDef def) {
        parsedFunctions.add(def);
    }
}
