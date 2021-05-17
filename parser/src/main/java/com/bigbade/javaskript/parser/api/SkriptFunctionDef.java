package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public abstract class SkriptFunctionDef implements ISkriptFunctionDef {
    @Getter
    private final Map<String, IValueTranslator<Object>> yamlValues;
    @Getter
    private final Map<ISkriptPattern, Object> patterns;

    static class SkriptBuildableMap<K, V> extends HashMap<K, V> {
        private static final long serialVersionUID = 1362798029702910L;

        private transient K[] tempKeys = null;
        private transient V[] tempValues = null;

        public SkriptBuildableMap<K, V> addEntry(K key, V value) {
            put(key, value);
            return this;
        }

        @SafeVarargs
        public final SkriptBuildableMap<K, V> addKeys(K... keys) {
            if(tempValues != null) {
                if(tempValues.length != keys.length) {
                    throw new IllegalArgumentException("Keys vargs has wrong size!");
                }
                for(int i = 0; i < tempValues.length; i++) {
                    put(keys[i], tempValues[i]);
                }
            } else {
                this.tempKeys = keys;
            }
            return this;
        }

        @SafeVarargs
        public final SkriptBuildableMap<K, V> addValues(V... values) {
            if(tempKeys != null) {
                if(tempKeys.length != values.length) {
                    throw new IllegalArgumentException("Keys vargs has wrong size!");
                }
                for(int i = 0; i < tempKeys.length; i++) {
                    put(tempKeys[i], values[i]);
                }
            } else {
                this.tempValues = values;
            }
            return this;
        }
    }
}
