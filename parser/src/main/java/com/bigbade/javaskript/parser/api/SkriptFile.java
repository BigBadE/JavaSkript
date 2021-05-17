package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
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
    private final List<ISkriptDef> skriptDefs = new ArrayList<>();

    @Override
    public void addSkriptDef(ISkriptDef def) {
        skriptDefs.add(def);
    }
}
