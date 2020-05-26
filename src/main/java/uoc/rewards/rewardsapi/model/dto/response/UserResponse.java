package uoc.rewards.rewardsapi.model.dto.response;

public class UserResponse {

    private Integer id;
    private String email;
    private String name;
    private String lastName;
    private String phone;
    private Integer points;

    public UserResponse(Integer id, String email, String name, String lastName, String phone, Integer points) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.points = points;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getPoints() {
        return points;
    }
}
