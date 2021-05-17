package software.bigbade.javaskript.parser.utils;

import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.objects.MethodLineConverter;

public final class VariableUtils {
    private VariableUtils() {}

    public static void setVariable(IScriptParser parser, String variable) {
        MethodLineConverter<?> lineConverter = parser.getLineConverter().orElseThrow(() ->
                new IllegalStateException("Cannot set variable without a parser line converter!"));
        if(variable.charAt(0) == '{') {
            String listName = variable.substring(1, variable.indexOf(':'));
            variable = variable.substring(3+listName.length());
            //Check if it is a hash map
            int index = variable.indexOf(':');
            if(index != -1) {
                String key = variable.substring(0, index+1);
                String value = variable.substring(index+2);
            } else {

            }
        } else {
            lineConverter.setVariable(lineConverter.getLocalVariable(variable));
        }
    }
}
