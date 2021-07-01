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
    private final List<IParsingDef> skriptDefs = new ArrayList<>();

    @Override
    public void addSkriptDef(IParsingDef def) {
        skriptDefs.add(def);
    }
}
