package uoc.rewards.rewardsapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uoc.rewards.rewardsapi.model.entity.Organisation;
import uoc.rewards.rewardsapi.model.entity.User;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

    @Query("SELECT o FROM Organisation o WHERE lower(o.email) = lower(:email)")
    Optional<Organisation> findByEmail(@Param("email") String email);

    @Query("SELECT o FROM Organisation o WHERE lower(o.email) = lower(:email) AND o.password = lower(:password)")
    Optional<Organisation> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

}