package uoc.rewards.rewardsapi.security.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * A JSON object that contains the claims conveyed by the JWT.
 * https://tools.ietf.org/html/rfc7519#section-4.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JWT {

    @JsonProperty(required = true)
    private String iss;

    @JsonProperty
    private String sub;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long exp;

    @JsonProperty(required = true)
    private long iat;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }
}
