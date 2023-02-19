package exceptions;

public class CouldNotSweepSnowException extends RuntimeException {
    private final String errorMessage;

    public CouldNotSweepSnowException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
