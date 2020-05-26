package uoc.rewards.rewardsapi.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="historic_income")
public class HistoricIncome extends Historic {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Integer points;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}
