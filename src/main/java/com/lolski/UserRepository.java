package com.lolski;

import java.util.Optional;
import java.util.Set;

/**
 * Hello world!
 */
public interface UserRepository {
    void init();

    void insert(User user);
    void insertIfNotExists(User user);

    void update(User updated);
    void updateIfAgeIsNull(User updated);

    void delete(String id);

    Optional<User> findById(String id);
    Set<User> findByAge(int age);

    Set<User> list();
}
