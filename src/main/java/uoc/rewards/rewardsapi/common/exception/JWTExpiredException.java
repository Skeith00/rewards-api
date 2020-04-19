package uoc.rewards.rewardsapi.common.exception;

public class JWTExpiredException extends UnauthorizedException {

    public JWTExpiredException(String msg) {
        super(msg);
    }
}
