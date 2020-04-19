package uoc.rewards.rewardsapi.model.dto.request;

public class JWTValidationRequest {

    private String jwtToken;
    private String app;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
