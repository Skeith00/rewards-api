package uoc.rewards.rewardsapi.model.dto.response;

import java.util.ArrayList;
import java.util.List;

public class SingleUserResponse extends UserResponse{

    private List<AvailableDeal> availableDeals = new ArrayList<>();

    public SingleUserResponse(Integer id, String email, String name, String lastName, String phone, Integer points, List<AvailableDeal> availableDeals) {
        super(id, email, name, lastName, phone, points);
        this.availableDeals = availableDeals;
    }

    public SingleUserResponse(Integer id, String email, String name, String lastName, String phone, Integer points) {
        super(id, email, name, lastName, phone, points);
    }

    public List<AvailableDeal> getAvailableDeals() {
        return availableDeals;
    }

    public static class AvailableDeal {
        private final Integer id;
        private final String name;

        public AvailableDeal(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
