package exceptions;

public class PackageAlreadyPickedUpException extends RuntimeException {
    public PackageAlreadyPickedUpException(String message) {
        super(message);
    }
}
