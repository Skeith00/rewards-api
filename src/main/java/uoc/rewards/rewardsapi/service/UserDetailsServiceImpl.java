package uoc.rewards.rewardsapi.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uoc.rewards.rewardsapi.model.entity.Organisation;
import uoc.rewards.rewardsapi.repository.OrganisationRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private OrganisationRepository organisationRepository;

    public UserDetailsServiceImpl(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Organisation> organisation = organisationRepository.findByEmail(username);
        if (!organisation.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(organisation.get().getEmail(), organisation.get().getPassword(), emptyList());
    }
}