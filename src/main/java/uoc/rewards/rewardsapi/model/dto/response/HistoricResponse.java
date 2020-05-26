package uoc.rewards.rewardsapi.model.dto.response;

import java.util.Date;

public class HistoricResponse {

    private String name;
    private Integer points;
    private Date date;

    public HistoricResponse(String name, Integer points, Date date) {
        this.name = name;
        this.points = points;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
