package uoc.rewards.rewardsapi.model.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Abstract class for Entities with ID
 */
@MappedSuperclass
public abstract class IdentifiableEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public void resetId() {
        this.id = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
