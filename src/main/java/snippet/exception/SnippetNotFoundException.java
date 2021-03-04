package snippet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SnippetNotFoundException extends RuntimeException {
    public SnippetNotFoundException() {
        super("Snippet not found");
    }
}
