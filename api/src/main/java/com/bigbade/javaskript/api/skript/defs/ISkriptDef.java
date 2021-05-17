package com.bigbade.javaskript.api.skript.defs;

import java.util.Map;
import java.util.Optional;

/**
 * Definitions are the top-level elements.
 * An example of a ISkriptDef is an event listener (such as on script load) or the configuration block.
 */
public interface ISkriptDef {
    /**
     * Returns a built key/value map.
     * @return Key/value map
     */
    Map<String, ?> getKeyValues();

    /**
     * Gets the data associated with the pattern.
     * @return Associated data
     */
    Optional<?> getData();
}
