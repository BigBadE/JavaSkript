package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.compiler.variables.Loadable;

@RequiredArgsConstructor
public class LoadVariableCall implements BasicInstruction {
    private final LocalVariable<?> variable;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor visitor) {
        if(!(variable instanceof Loadable)) {
            throw new IllegalArgumentException("Variable " + variable + " not a Loadable subclass");
        }
        ((Loadable) variable).loadVariable(builder, visitor);
    }
}
