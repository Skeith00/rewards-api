package uoc.rewards.rewardsapi.common.exception;

public class ExpiredJWTException extends UnauthorizedException {

    public ExpiredJWTException(String msg) {
        super(msg);
    }
}
