package uoc.rewards.rewardsapi.common.exception;

public class ExpiredJWTException extends ForbiddenException {

    public ExpiredJWTException(String msg) {
        super(msg);
    }
}
