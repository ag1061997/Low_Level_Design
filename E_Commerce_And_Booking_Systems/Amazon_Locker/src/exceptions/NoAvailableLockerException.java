package exceptions;

public class NoAvailableLockerException extends RuntimeException {
    public NoAvailableLockerException(String message) {
        super(message);
    }
}
