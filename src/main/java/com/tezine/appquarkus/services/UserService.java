package com.tezine.appquarkus.services;

import java.util.List;
import com.tezine.appquarkus.entities.User;
import com.tezine.appquarkus.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    @Transactional
    public User updateUser(long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser == null)
            return null;
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        userRepository.persist(existingUser);
        return existingUser;
    }

    @Transactional
    public long saveUser(User user) {
        userRepository.persistAndFlush(user);
        return user.getId();
    }

    @Transactional
    public boolean deleteUser(long id) {
        var user = getUserById(id);
        if (user == null)
            return false;
        userRepository.delete(user);
        return true;
    }
}
