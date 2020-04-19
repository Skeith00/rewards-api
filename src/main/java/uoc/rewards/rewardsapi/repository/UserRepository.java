package uoc.rewards.rewardsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uoc.rewards.rewardsapi.model.entity.Organisation;
import uoc.rewards.rewardsapi.model.entity.User;

import java.util.Optional;
import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE lower(u.email) = lower(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.organisation = :org")
    Set<User> findByOrg(@Param("org") Organisation org);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.organisation = :org")
    Optional<User> findByIdAndOrganisation(@Param("id") Integer id, @Param("org") Organisation org);

}
