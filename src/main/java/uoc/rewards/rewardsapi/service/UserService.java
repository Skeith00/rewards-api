package uoc.rewards.rewardsapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uoc.rewards.rewardsapi.common.exception.BadRequestException;
import uoc.rewards.rewardsapi.common.exception.NotFoundException;
import uoc.rewards.rewardsapi.model.dto.request.UserCreationRequest;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.model.entity.*;
import uoc.rewards.rewardsapi.repository.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private OrganisationRepository organisationRepository;
    private DealRepository dealRepository;
    private HistoricIncomeRepository historicIncomeRepository;
    private HistoricOutcomeRepository historicOutcomeRepository;

    public UserService(UserRepository userRepository,
                       OrganisationRepository organisationRepository,
                       DealRepository dealRepository,
                       HistoricIncomeRepository historicIncomeRepository,
                       HistoricOutcomeRepository historicOutcomeRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.dealRepository = dealRepository;
        this.historicIncomeRepository = historicIncomeRepository;
        this.historicOutcomeRepository = historicOutcomeRepository;
    }

    @Transactional
    public void createUser(UserCreationRequest userCreationRequest, Integer orgId) {
        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(() ->
                new NotFoundException(String.format("Organisation with id %d not found", orgId)));
        Optional<User> user = userRepository.findByEmail(userCreationRequest.getEmail());

        if(user.isPresent() && user.get().getOrganisation() == organisation) {
            throw new BadRequestException(String.format("Duplicated User %s", userCreationRequest.getEmail()));
        }

        userRepository.save(buildUser(userCreationRequest, organisation));
    }

    @Transactional
    public void pay(int userId, double price, Integer dealId, Integer orgId) {
        Organisation org = organisationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with id %d not found", orgId)));
        User user = userRepository.findByIdAndOrganisation(userId, org).orElseThrow(() -> new NotFoundException(String.format("User with id %d not found in Org %d", userId, orgId)));

        HistoricIncome historicIncome = new HistoricIncome();
        historicIncome.setDate(new Date());
        historicIncome.setPoints(calculatePoints(price));
        user.addPoints(calculatePoints(price));

        if(dealId != null) {
            Deal deal = dealRepository.findByIdAndOrganisation(dealId, org)
                    .orElseThrow(() -> new NotFoundException(String.format("Deal with id %d not found in Org %d", dealId, orgId)));
            HistoricOutcome historicOutcome = new HistoricOutcome();
            historicOutcome.setDate(new Date());
            historicOutcome.setDeal(deal);
            user.subtractPoints(calculatePoints(deal.getPoints()));
            historicOutcomeRepository.save(historicOutcome);
        }

        user.addHistoricIncome(historicIncome);
        userRepository.save(user);
        historicIncomeRepository.save(historicIncome);

    }

    public Set<UserResponse> getUsersByOrg(Integer orgId) {
        Organisation org = organisationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with id %d not found", orgId)));

        Set<User> usersByOrg = userRepository.findByOrg(org);
        Set<UserResponse> userResponses = usersByOrg.stream()
                .map(user -> new UserResponse(
                    user.getEmail(),
                    user.getName(),
                    user.getLastName(),
                    user.getPhone(),
                    user.getPoints()))
                .collect(Collectors.toSet());

        return userResponses;
    }

    private User buildUser(UserCreationRequest userCreationRequest, Organisation org) {
        User user = new User(
                userCreationRequest.getEmail(),
                userCreationRequest.getName(),
                userCreationRequest.getLastName(),
                userCreationRequest.getPhone());

        user.setOrganisation(org);
        return user;
    }

    public int calculatePoints(double price) {
        return (int) Math.round(price);
    }
}
