package software.bigbade.javaskript.api.exception;

public class EmptyStackException extends RuntimeException {
    public EmptyStackException() {
        super("Tried to pop empty stack!");
    }
}
