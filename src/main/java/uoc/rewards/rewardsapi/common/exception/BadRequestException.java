package uoc.rewards.rewardsapi.common.exception;



public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Exception cause) {
        super(message, cause);
    }
}