package com.bigbade.javaskript.java.defs;

import com.bigbade.javaskript.api.java.defs.ClassMembers;
import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.java.util.Modifiers;
import com.bigbade.javaskript.api.skript.addon.SingleTranslatorDef;
import com.bigbade.javaskript.api.skript.addon.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;

@SkriptPattern(pattern="On script load")
public class ScriptLoadDef extends SingleTranslatorDef<ICodeDef> {
    @Override
    public void operateOnDef(ICodeDef startingValue, int patternData, IPackageDef mainPackage) {
        IMethodDef methodDef = mainPackage.getJavaFile("Main").getMainClass().getClassMember(ClassMembers.METHODS, "main");
        methodDef.setModifiers(new Modifiers(Modifiers.ModifierTypes.PUBLIC, Modifiers.ModifierTypes.STATIC));
        methodDef.addParameters(getVariables());
    }

    @Override
    public void setupVariables(IVariableFactory factory) {
        addVariable(factory.createParameter("args", factory.getClassType(String[].class)));
    }

    @Override
    public IValueTranslator<ICodeDef> getTranslator(ITranslatorFactory factory) {
        return factory.getCodeTranslator();
    }
}
