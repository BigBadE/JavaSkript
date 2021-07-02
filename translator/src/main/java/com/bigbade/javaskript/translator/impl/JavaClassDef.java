package com.bigbade.javaskript.translator.impl;

import com.bigbade.javaskript.api.java.defs.ClassMembers;
import com.bigbade.javaskript.api.java.defs.IClassDef;
import com.bigbade.javaskript.api.java.defs.IClassMember;
import com.bigbade.javaskript.api.java.defs.IJavaFile;
import com.bigbade.javaskript.api.java.util.Modifiers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JavaClassDef implements IClassDef {
    private final Map<ClassMembers, IClassMember> classMembers = new EnumMap<>(ClassMembers.class);

    @Getter
    private final IJavaFile javaFile;

    @Getter
    private final String className;

    @Getter
    @Setter
    private Modifiers modifiers;


    @Override
    public void addClassMember(IClassMember member) {
        classMembers.put(member.getType(), member);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IClassMember> List<T> getMembersOfType(ClassMembers memberType) {
        return (List<T>) classMembers.get(memberType);
    }

    @Override
    public <T extends IClassMember> T getClassMember(ClassMembers memberType, String name) {
        List<T> members = getMembersOfType(memberType);
        if(members != null) {
            for (T member : members) {
                if (member.getName().equals(name)) {
                    return member;
                }
            }
        }

        switch (memberType) {
            //TODO add class members here
            default -> throw new IllegalStateException("Unknown class member " + memberType);
        }
    }
}
