package com.bigbade.javaskript.api.java.util;

import com.bigbade.javaskript.api.java.defs.ClassMembers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Modifier;

/**
 * Represents a modifier on a definition. Also allows checking if the modifiers are legal on the construct.
 */
@SuppressWarnings("unused")
public class Modifiers {
    private static final int CLASS_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
            Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.STRICT;
    private static final int INTERFACE_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
            Modifier.ABSTRACT | Modifier.STATIC | Modifier.STRICT;
    private static final int CONSTRUCTOR_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
    private static final int METHOD_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
            Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL | Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.STRICT;
    private static final int FIELD_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
            Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT | Modifier.VOLATILE;
    private static final int PARAMETER_MODIFIERS = Modifier.FINAL;
    private static final int IMPORT_MODIFIERS = Modifier.STATIC;

    //Final modifier mask
    @Getter
    private final int mask;

    @RequiredArgsConstructor
    public enum ModifierTypes {
        PUBLIC(0x000001, 0x000006),
        PRIVATE(0x000002, 0x000005),
        PROTECTED(0x000004, 0x000003),
        STATIC(0x000008, 0x000000),
        FINAL(0x000010, 0x000000),
        SYNCRONIZED(0x000020, 0x000000),
        VOLATILE(0x000040, 0x000000),
        TRANSIENT(0x000080, 0x000000),
        NATIVE(0x000100, 0x000000),
        INTERFACE(0x000200, 0x000400),
        //Cannot be on methods in an interface
        ABSTRACT(0x000400, 0x000200),
        //Cannot be on methods in an interface and abstract methods
        STRICT(0x000800, 0x000000);

        private final int mask;
        private final int conflicts;
    }

    static class ModifierBuilder {
        //Non-final modifier mask
        private int mask = 0;

        /**
         * Adds the modifier to the builder.
         * @param types Modifier to add
         * @return Current builder
         * @throws IllegalArgumentException if the modifier conflicts with previous modifiers, like public and private
         */
        public ModifierBuilder addModifier(ModifierTypes types) {
            if((types.conflicts & mask) != 0) {
                throw new IllegalArgumentException("Conflicting modifier " + types);
            }
            mask |= types.mask;
            return this;
        }

        /**
         * Builds the builder into a finalized modifiers.
         * @return Modifiers of the builder
         */
        public Modifiers build() {
            return new Modifiers(mask);
        }
    }

    /**
     * Constructs modifiers given a certain mask.
     * This does not check the validity of the modifiers!
     * @param mask Modifier mask
     */
    public Modifiers(int mask) {
        this.mask = mask;
    }

    /**
     * Constructs modifiers from an array of modifier types.
     * @param types Modifiers to add
     * @throws IllegalArgumentException If conflicting modifiers are added
     */
    public Modifiers(ModifierTypes... types) {
        int tempMask = 0;
        for(ModifierTypes modifier : types) {
            tempMask |= modifier.mask;
            if((modifier.conflicts & tempMask) != 0) {
                throw new IllegalArgumentException("Conflicting modifier " + modifier);
            }
        }
        this.mask = tempMask;
    }

    /**
     * Checks if this class has that modifier.
     * @param modifier Modifier to check against
     * @return True if that modifier was added, false if not
     */
    public boolean hasModifier(ModifierTypes modifier) {
        return (mask & modifier.mask) == 0;
    }

    /**
     * Checks if the modifiers are legal on the given class member.
     * @param member Class member to check against
     * @return true if the combination is legal, false if not
     */
    public boolean isLegal(ClassMembers member) {
        switch (member) {
            case CLASS:
                return (CLASS_MODIFIERS | mask) == CLASS_MODIFIERS;
            case INTERFACE:
                return (INTERFACE_MODIFIERS | mask) == INTERFACE_MODIFIERS;
            case METHODS:
                return (METHOD_MODIFIERS | mask) == METHOD_MODIFIERS;
            case CONSTRUCTORS:
                return (CONSTRUCTOR_MODIFIERS | mask) == CONSTRUCTOR_MODIFIERS;
            case FIELDS:
                return (FIELD_MODIFIERS | mask) == FIELD_MODIFIERS;
            default:
                return mask == 0;
        }
    }

    /**
     * Checks if this modifier combination is legal on parameters.
     * @return True if the modifiers are legal, false if not
     */
    public boolean isLegalParameter() {
        return (PARAMETER_MODIFIERS | mask) == PARAMETER_MODIFIERS;
    }
}
