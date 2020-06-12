package uoc.rewards.rewardsapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uoc.rewards.rewardsapi.common.exception.BadRequestException;
import uoc.rewards.rewardsapi.common.exception.NotFoundException;
import uoc.rewards.rewardsapi.model.dto.request.UserRequest;
import uoc.rewards.rewardsapi.model.dto.response.HistoricResponse;
import uoc.rewards.rewardsapi.model.dto.response.SingleUserResponse;
import uoc.rewards.rewardsapi.model.dto.response.UserResponse;
import uoc.rewards.rewardsapi.model.entity.*;
import uoc.rewards.rewardsapi.repository.*;

import java.util.*;
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

    public void createUser(UserRequest userCreationRequest, String orgEmail) {
        Organisation organisation = organisationRepository.findByEmail(orgEmail).orElseThrow(() ->
                new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));
        Optional<User> user = userRepository.findByEmail(userCreationRequest.getEmail());

        if(user.isPresent() && user.get().getOrganisation() == organisation) {
            throw new BadRequestException(String.format("Duplicated User %s", userCreationRequest.getEmail()));
        }
        userRepository.save(buildUser(userCreationRequest, organisation));
    }

    @Transactional
    public void editUser(int userId, UserRequest userRequest, String orgEmail) {
        Organisation organisation = organisationRepository.findByEmail(orgEmail).orElseThrow(() ->
                new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));
        User user = userRepository.findByIdAndOrganisation(userId, organisation)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        user.setPhone(userRequest.getPhone());
        user.setName(userRequest.getName());
        user.setLastName(userRequest.getLastName());
    }

    @Transactional
    public void deleteUser(Integer userId, String orgEmail) {
        Organisation org = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));
        User user = userRepository.findByIdAndOrganisation(userId, org)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        userRepository.delete(user);
    }

    @Transactional
    public void pay(int userId, double price, Integer dealId, String orgEmail) {
        Organisation org = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));
        User user = userRepository.findByIdAndOrganisation(userId, org).orElseThrow(() -> new NotFoundException(String.format("User with id %d not found in Org %d", userId, org.getName())));

        HistoricIncome historicIncome = new HistoricIncome();
        historicIncome.setDate(new Date());
        historicIncome.setPoints(calculatePoints(price));
        historicIncome.setUser(user);

        user.addPoints(calculatePoints(price));

        if(dealId != null && dealId != 0) {
            Deal deal = dealRepository.findByIdAndOrganisation(dealId, org)
                    .orElseThrow(() -> new NotFoundException(String.format("Deal with id %d not found in Org %d", dealId, org.getName())));
            HistoricOutcome historicOutcome = new HistoricOutcome();
            historicOutcome.setDate(new Date());
            historicOutcome.setDeal(deal);
            historicOutcome.setUser(user);
            user.subtractPoints(calculatePoints(deal.getPoints()));
            historicOutcomeRepository.save(historicOutcome);
        }

        user.addHistoricIncome(historicIncome);
        historicIncomeRepository.save(historicIncome);
        userRepository.save(user);
    }

    public List<UserResponse> getUsersByOrg(String orgEmail) {
        Organisation org = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with id %d not found", orgEmail)));

        List<User> usersByOrg = userRepository.findByOrg(org);
        return usersByOrg.stream()
                .map(user -> new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getLastName(),
                    user.getPhone(),
                    user.getPoints()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse getUserByIdAndOrg(Integer userId, String orgEmail) {
        Organisation org = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));

        User user = userRepository.findByIdAndOrganisation(userId, org)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        Set<Deal> deals = org.getDeals();
        List<SingleUserResponse.AvailableDeal> filteredDeals = deals.stream()
                .filter(deal -> (deal.getPoints() <= user.getPoints() && deal.getExpiryDate().after(new Date())))
                .map(deal-> new SingleUserResponse.AvailableDeal(deal.getId(), deal.getName()))
                .sorted()
                .collect(Collectors.toList());
        return new SingleUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLastName(),
                user.getPhone(),
                user.getPoints(),
                filteredDeals
        );
    }

    @Transactional
    public List<HistoricResponse> getHistoricByUserIdAndOrg(Integer userId, String orgEmail) {
        Organisation org = organisationRepository.findByEmail(orgEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Organisation with email %d not found", orgEmail)));

        User user = userRepository.findByIdAndOrganisation(userId, org)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<HistoricResponse> historicResponses = user.getHistoricIncomes().stream()
                .map(historicIncome -> new HistoricResponse("Consumption", historicIncome.getPoints(), historicIncome.getDate()))
                .collect(Collectors.toList());
        historicResponses.addAll(
                user.getHistoricOutcomes().stream()
                        .map(historicOutcome -> {
                            Deal deal = historicOutcome.getDeal();
                            return new HistoricResponse(deal.getName(), deal.getPoints(), historicOutcome.getDate());
                        })
                        .collect(Collectors.toList())
        );
        historicResponses.sort(Comparator.comparing(HistoricResponse::getDate));
        return historicResponses;
    }

    private User buildUser(UserRequest userCreationRequest, Organisation org) {
        User user = new User(
                userCreationRequest.getEmail(),
                userCreationRequest.getName(),
                userCreationRequest.getLastName(),
                userCreationRequest.getPhone());

        user.setOrganisation(org);
        return user;
    }

    private int calculatePoints(double price) {
        return (int) Math.round(price);
    }
}
