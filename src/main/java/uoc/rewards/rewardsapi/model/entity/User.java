package uoc.rewards.rewardsapi.model.entity;

import uoc.rewards.rewardsapi.common.exception.ServiceException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="buyer")
public class User extends IdentifiableEntity {

    private String email;
    private String name;

    @Column(name = "last_name")
    private String lastName;
    private String phone;

    private int points = 0;

    @ManyToOne
    @JoinColumn(name="org_id", nullable = false)
    private Organisation organisation;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricIncome> historicIncomes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricOutcome> historicOutcomes = new ArrayList<>();

    public User(String email, String name, String lastName, String phone) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void subtractPoints(int points) {
        if(this.points < points) {
            throw new ServiceException(String.format("Insufficient points for User %s", email));
        }
        this.points -= points;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<HistoricIncome> getHistoricIncomes() {
        return historicIncomes;
    }

    public void setHistoricIncomes(List<HistoricIncome> historicIncomes) {
        this.historicIncomes = historicIncomes;
    }

    public void addHistoricIncome(HistoricIncome historicIncome) {
        historicIncomes.add(historicIncome);
    }

    public List<HistoricOutcome> getHistoricOutcomes() {
        return historicOutcomes;
    }

    public void setHistoricOutcomes(List<HistoricOutcome> historicOutcomes) {
        this.historicOutcomes = historicOutcomes;
    }

    public void addHistoricOutcome(HistoricOutcome historicOutcome) {
        historicOutcomes.add(historicOutcome);
    }
}
