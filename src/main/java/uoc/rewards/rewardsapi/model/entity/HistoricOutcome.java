package uoc.rewards.rewardsapi.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="historic_outcome")
public class HistoricOutcome extends Historic {

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Deal deal;

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }
}
