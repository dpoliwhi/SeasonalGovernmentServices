package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.dto.LoginDto;
import ru.didenko.smartconsulting.seasonalservices.exceptions.ConstraintsException;
import ru.didenko.smartconsulting.seasonalservices.model.User;
import ru.didenko.smartconsulting.seasonalservices.repository.UserRepository;
import ru.didenko.smartconsulting.seasonalservices.service.userDetails.CustomUserDetailsService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService extends GenericService<User>{

    /**
     * Values from properties. Id of each role must matches with ids in "role" table in DB
     */
    @Value("${seasonalservices.userId}")
    private Long USER_ID;

    @Value("${seasonalservices.managerId}")
    private Long MANAGER_ID;

    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository repository;
    private final CustomUserDetailsService userDetailsService;

    public UserService(
            UserRepository repository,
            RoleService roleService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserRepository userRepository, CustomUserDetailsService userDetailsService) {
        super(repository);
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.repository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public User create(User object) throws ConstraintsException {
        object.setCreatedBy(object.getLastName());
        object.setRole(roleService.getOneById(USER_ID));
        object.setCreatedWhen(LocalDateTime.now());
        setDeletedAndUpdatedNull(object);
        object.setPassword(bCryptPasswordEncoder.encode(object.getPassword()));
        checkConstraintsOfNewUser(object);
        return super.create(object);
    }

    public User createManager(User object) throws ConstraintsException {
        object.setCreatedBy("ADMIN");
        object.setCreatedWhen(LocalDateTime.now());
        object.setRole(roleService.getOneById(MANAGER_ID));
        setDeletedAndUpdatedNull(object);
        object.setPassword(bCryptPasswordEncoder.encode(object.getPassword()));
        checkConstraintsOfNewUser(object);
        return super.create(object);
    }

    @Override
    public User update(User object) throws ConstraintsException {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy(object.getLastName());
        object.setUpdatedWhen(LocalDateTime.now());
        User user = getOneById(object.getId());
        object.setPassword(bCryptPasswordEncoder.encode(object.getPassword()));
        object.setRole(user.getRole());
        return super.update(object);
    }

    /**
     * Method to check password from LoginDto by user login
     */
    public boolean checkPassword(LoginDto loginDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getLogin());
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }


    private void checkConstraintsOfNewUser(User user) throws ConstraintsException {
//        User checkEmail = repository.findUserByEmail(user.getEmail());
//        if (!Objects.isNull(checkEmail)) {
//            throw new ConstraintsException("This mail is already registered in the system");
//        }
        User checkLogin = repository.findUserByLogin(user.getLogin());
        if (!Objects.isNull(checkLogin)) {
            throw new ConstraintsException("This login is already registered in the system");
        }
    }
}
