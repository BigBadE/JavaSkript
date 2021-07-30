package com.bigbade.javaskript.api.java.util;

/**
 * Represents a class or generic, usually used as a return type on a method or variable type.
 */
@SuppressWarnings("unused")
public interface IClassType {
    /**
     * Returns the simple name of the class (IClassType for this), or the identifier of a generic.
     * @return Simple name of this type
     */
    String getSimpleName();

    /**
     * Returns the qualified name of the class (com.bigbade...IClassType for this), or the string format of a generic
     * (such as &gt;T extends IClassType&lt;)
     * @return Qualified name of this type
     */
    String getQualifiedName();

    /**
     * Returns the internal name used by Java. Do not use unless you know what you are doing!
     */
    String getInternalName();

    /**
     * Returns if the other class type is the same type
     * @param other Other class type
     * @return If the types match
     */
    boolean isOfType(IClassType other);
}
