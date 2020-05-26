package uoc.rewards.rewardsapi.model.dto.request;

import javax.validation.constraints.NotEmpty;

public class PaymentRequest {

    @NotEmpty
    private Double price;
    private Integer dealId;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDealId() {
        return dealId;
    }

    public void setDealId(Integer dealId) {
        this.dealId = dealId;
    }
}
