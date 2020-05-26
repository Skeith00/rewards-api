package uoc.rewards.rewardsapi.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class DealResponse {

    private Integer id;
    private String name;
    private String description;
    private Integer points;

    @JsonProperty(value = "expiry_date")
    private Date expiryDate;

    public DealResponse(Integer id, String name, String description, Integer points, Date expiryDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.points = points;
        this.expiryDate = expiryDate;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPoints() {
        return points;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}
