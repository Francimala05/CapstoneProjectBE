package PizzaPazza.PizzaPazzaSecurity.model.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
