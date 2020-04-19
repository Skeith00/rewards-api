package uoc.rewards.rewardsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uoc.rewards.rewardsapi.model.entity.HistoricIncome;
import uoc.rewards.rewardsapi.model.entity.HistoricOutcome;


@Repository
public interface HistoricOutcomeRepository extends JpaRepository<HistoricOutcome, Integer> {

}
