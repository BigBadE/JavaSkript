package com.bigbade.javaskript.api.java.defs;

import com.bigbade.javaskript.api.java.util.Modifiers;

import java.util.List;

/**
 * Defines a class. Classes contain members, which all extend IClassMember
 * @see IClassMember
 * @see ClassMembers
 */
@SuppressWarnings("unused")
public interface IClassDef {
    /**
     * Adds the class member to the class
     * @param member Member to add
     */
    void addClassMember(IClassMember member);

    /**
     * Gets the class name
     */
    String getClassName();

    /**
     * Gets the modifiers
     * @return Class modifiers
     */
    Modifiers getModifiers();

    /**
     * Gets the class member from the class
     * @param memberType Type of member
     * @param <T> Member type
     * @return Members of that type contained by the class
     */
    <T extends IClassMember> List<T> getClassMember(ClassMembers memberType);

    /**
     * Gets or creates a class member with the name, what the name actually is depends on the class member.
     * @param memberType Member type
     * @param name Member name
     * @param <T> Class type associated with that member type
     * @return Class member with that name
     */
    <T extends IClassMember> T getClassMember(ClassMembers memberType, String name);
}
