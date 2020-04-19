package uoc.rewards.rewardsapi.common.exception;

/**
 * Triggered when a request is made with a revoked jwt.
 *
 * @author Seema <seema@redeye.co>.
 */
public class TokenBlacklistedException extends ForbiddenException {
    public TokenBlacklistedException(String msg, Throwable t) {
        super(msg, t);
    }

    public TokenBlacklistedException(String msg) {
        super(msg);
    }

    public TokenBlacklistedException(Throwable t) {
        super(t.getMessage(), t);
    }
}