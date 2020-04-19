package uoc.rewards.rewardsapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uoc.rewards.rewardsapi.common.exception.BadRequestException;
import uoc.rewards.rewardsapi.common.exception.NotFoundException;
import uoc.rewards.rewardsapi.model.dto.request.AddDealRequest;
import uoc.rewards.rewardsapi.model.dto.request.UserCreationRequest;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.model.entity.Deal;
import uoc.rewards.rewardsapi.model.entity.Organisation;
import uoc.rewards.rewardsapi.model.entity.User;
import uoc.rewards.rewardsapi.repository.DealRepository;
import uoc.rewards.rewardsapi.repository.OrganisationRepository;
import uoc.rewards.rewardsapi.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DealService {

    private DealRepository dealRepository;
    private OrganisationRepository organisationRepository;

    public DealService(DealRepository dealRepository, OrganisationRepository organisationRepository) {
        this.dealRepository = dealRepository;
        this.organisationRepository = organisationRepository;
    }

    @Transactional
    public void createDeal(AddDealRequest addDealRequest, Integer orgId) {
        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with id %d not found", orgId)));

        Deal newDeal = new Deal();
        newDeal.setName(addDealRequest.getName());
        newDeal.setDescription(addDealRequest.getDescription());
        newDeal.setExpiryDate(addDealRequest.getExpiryDate());
        newDeal.setPoints(addDealRequest.getPoints());
        newDeal.setOrganisation(organisation);
        dealRepository.save(newDeal);
    }
}
