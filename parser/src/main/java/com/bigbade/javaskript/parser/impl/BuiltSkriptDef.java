package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BuiltSkriptDef<T> implements ISkriptDef {
    @Getter
    private final Map<String, ?> keyValues;

    @Nullable
    private final T data;

    @Override
    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }
}
