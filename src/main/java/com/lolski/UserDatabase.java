package com.lolski;

import java.util.Set;

/**
 * Hello world!
 */
public interface UserDatabase {
    void init();

    // must be unique
    void insertIfNotExists(User user);

    void delete(String id);

    void updateIfAgeIsNull(User updated);

    Set<User> list();
}
