package com.bigbade.javaskript.java.effects;

import com.bigbade.javaskript.api.skript.addon.SkriptPattern;

public class BroadcastExpression {
    @SkriptPattern(pattern="broadcast %object%")
    public void broadcast(Object broadcasting) {
        System.out.println(broadcasting);
    }
}
