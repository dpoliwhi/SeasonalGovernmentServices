package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.stereotype.Repository;
import ru.didenko.smartconsulting.seasonalservices.model.User;

@Repository
public interface UserRepository extends GenericRepository<User> {

    User findUserByLogin(String login);
}
