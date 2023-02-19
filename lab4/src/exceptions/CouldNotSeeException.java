package exceptions;

public class CouldNotSeeException extends Exception {
    private final String errorMessage;

    public CouldNotSeeException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
