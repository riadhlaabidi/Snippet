package snippet.exception;

public class CodeNotFoundException extends RuntimeException {
    public CodeNotFoundException() {
        super("Code snippet not found");
    }
}
