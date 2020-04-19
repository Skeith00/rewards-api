package uoc.rewards.rewardsapi.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="historic_income")
public class HistoricIncome extends Historic {

    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}
