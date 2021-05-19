package com.bigbade.javaskript.api.skript.addon;

import java.util.Map;

public interface ISkriptSerializerAddon<T> {
    /**
     * Class type to serialize.
     * @return Type to serialize
     */
    Class<?> getTarget();

    /**
     * Serialized object in a map of objects and strings
     * @param object Object to serialize
     * @return Deserialized map, all objects should also have a serializer
     */
    Map<String, Object> serialize(T object);

    /**
     * Deserialized object from a map of objects and strings
     * @param data Serialized data
     * @return Deserialized object
     */
    T deserialize(Map<String, Object> data);
}
