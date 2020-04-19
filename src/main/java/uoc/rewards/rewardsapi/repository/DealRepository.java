package uoc.rewards.rewardsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uoc.rewards.rewardsapi.model.entity.Deal;
import uoc.rewards.rewardsapi.model.entity.Organisation;

import java.util.Optional;


@Repository
public interface DealRepository extends JpaRepository<Deal, Integer> {

    @Query("SELECT d FROM Deal d WHERE d.id = :id AND d.organisation = :org")
    Optional<Deal> findByIdAndOrganisation(@Param("id") Integer id, @Param("org") Organisation org);
}
