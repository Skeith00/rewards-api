package uoc.rewards.rewardsapi.service;

import org.springframework.stereotype.Service;
import uoc.rewards.rewardsapi.common.exception.NotFoundException;
import uoc.rewards.rewardsapi.model.dto.request.AddDealRequest;
import uoc.rewards.rewardsapi.model.dto.response.DealResponse;
import uoc.rewards.rewardsapi.model.entity.Deal;
import uoc.rewards.rewardsapi.model.entity.Organisation;
import uoc.rewards.rewardsapi.repository.DealRepository;
import uoc.rewards.rewardsapi.repository.OrganisationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealService {

    private DealRepository dealRepository;
    private OrganisationRepository organisationRepository;

    public DealService(DealRepository dealRepository, OrganisationRepository organisationRepository) {
        this.dealRepository = dealRepository;
        this.organisationRepository = organisationRepository;
    }

    public void createDeal(AddDealRequest addDealRequest, String orgEmail) {
        Organisation organisation = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));

        Deal newDeal = new Deal();
        newDeal.setName(addDealRequest.getName());
        newDeal.setDescription(addDealRequest.getDescription());
        newDeal.setExpiryDate(addDealRequest.getExpiryDate());
        newDeal.setPoints(addDealRequest.getPoints());
        newDeal.setOrganisation(organisation);
        dealRepository.save(newDeal);
    }


    public List<DealResponse> getDealsByOrg(String orgEmail) {
        Organisation organisation = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));
        List<Deal> deals = dealRepository.findByOrganisation(organisation);
        return deals.stream()
                .map(deal -> new DealResponse(
                        deal.getId(),
                        deal.getName(),
                        deal.getDescription(),
                        deal.getPoints(),
                        deal.getExpiryDate()))
                .collect(Collectors.toList());
    }
}
