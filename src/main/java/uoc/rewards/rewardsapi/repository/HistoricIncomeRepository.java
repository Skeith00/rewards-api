package uoc.rewards.rewardsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uoc.rewards.rewardsapi.model.entity.Deal;
import uoc.rewards.rewardsapi.model.entity.HistoricIncome;


@Repository
public interface HistoricIncomeRepository extends JpaRepository<HistoricIncome, Integer> {

}
