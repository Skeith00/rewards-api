package uoc.rewards.rewardsapi.model.dto.response;

public class UserResponse {

    private String email;
    private String name;
    private String lastName;
    private String phone;
    private Integer points;

    public UserResponse(String email, String name, String lastName, String phone, Integer points) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
