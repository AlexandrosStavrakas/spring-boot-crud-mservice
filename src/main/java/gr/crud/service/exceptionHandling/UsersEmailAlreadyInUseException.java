package gr.crud.service.exceptionHandling;

public class UsersEmailAlreadyInUseException extends RuntimeException {

    public UsersEmailAlreadyInUseException(String message) {
        super(message);
    }
}
