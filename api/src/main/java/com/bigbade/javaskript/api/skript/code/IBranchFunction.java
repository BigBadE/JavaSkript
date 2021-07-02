package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;

import java.lang.reflect.Method;

public interface IBranchFunction extends ISkriptInstruction {
    IBranchFunctionDef getFunctionDef();

    Method getMethod();
}
