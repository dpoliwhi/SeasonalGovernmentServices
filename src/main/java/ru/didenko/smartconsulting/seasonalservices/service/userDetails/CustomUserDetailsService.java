package ru.didenko.smartconsulting.seasonalservices.service.userDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.User;
import ru.didenko.smartconsulting.seasonalservices.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class implements the UserDetailsService interface with loading User
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Values from properties. Id of each role must matches with ids in "role" table in DB
     */
    @Value("${seasonalservices.userId}")
    private Long USER_ID;

    private final UserRepository userRepository;

    /**
     * Login, Password and Role of ADMIN are setting from properties
     */
    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminPassword;
    @Value("${spring.security.user.roles}")
    private String adminRole;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method checks the username of authenticated user and returns the UserDetails object
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(adminRole));
            return new CustomUserDetails(0L, adminUserName, adminPassword, authorities);
        } else {
            User user = userRepository.findUserByLogin(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(Objects.equals(user.getRole().getId(), USER_ID) ? "ROLE_USER" : "ROLE_MANAGER"));
            return new CustomUserDetails(user.getId(), username, user.getPassword(), authorities);
        }
    }
}
