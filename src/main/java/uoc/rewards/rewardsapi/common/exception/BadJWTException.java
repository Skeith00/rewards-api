package uoc.rewards.rewardsapi.common.exception;


public class BadJWTException extends ForbiddenException {
    public BadJWTException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadJWTException(String msg) {
        super(msg);
    }

    public BadJWTException(Throwable t) {
        super(t.getMessage(), t);
    }
}