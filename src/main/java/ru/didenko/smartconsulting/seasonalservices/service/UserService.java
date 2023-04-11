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


/**
 * Class-service with main logic for User
 */
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

    /**
     * Create user
     * <ul>
     * <li>With creation user-object method set the CreatedBy field
     * <li>If login is already used, method throws ConstraintsException with message
     * <li>If email is already used, method throws ConstraintsException with message
     * <li>Also, automatically set the Role value with USER_ROLE
     * <li>Password is encoded with BCryptEncoder</ul>
     */
    @Override
    public User create(User object) throws ConstraintsException {
        object.setCreatedBy(object.getLogin());
        object.setRole(roleService.getOneById(USER_ID));
        object.setCreatedWhen(LocalDateTime.now());
        setDeletedAndUpdatedNull(object);
        object.setPassword(bCryptPasswordEncoder.encode(object.getPassword()));
        checkConstraintsOfNewUser(object);
        return super.create(object);
    }

    /**
     * Create manager.<p>
     * Only Admin can create managers so with creation the CreatedBy field set with value "ADMIN".<p>
     * Also, automatically set the Role value with ROLE_MANAGER<p>
     * If login is already used, method throws ConstraintsException with message<p>
     * If email is already used, method throws ConstraintsException with message<p>
     * Password is encoded with BCryptEncoder.
     */
    public User createManager(User object) throws ConstraintsException {
        object.setCreatedBy("ADMIN");
        object.setCreatedWhen(LocalDateTime.now());
        object.setRole(roleService.getOneById(MANAGER_ID));
        setDeletedAndUpdatedNull(object);
        object.setPassword(bCryptPasswordEncoder.encode(object.getPassword()));
        checkConstraintsOfNewUser(object);
        return super.create(object);
    }

    /**
     * Update user.<p>
     * With updating object method set the CreatedBy and CreatedWhen fields from existing object.<p>
     * UpdatedBy field set with login of updated user<p>
     * Also, automatically set the Role value from the existing user-object<p>
     * Password is encoded with BCryptEncoder.
     */
    @Override
    public User update(User object) throws ConstraintsException {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy(object.getLogin());
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

    /**
     * Method checks constraints of creating User<p>
     * Login and email of each user have to be unique<p><p>
     *
     * Checking email constraint disabled during development
     */
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
