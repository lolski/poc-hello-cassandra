package com.lolski;

import java.util.List;

/**
 * Hello world!
 */
public class UserDatabase {
    private UserDatabaseCassandra cassandra;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public UserDatabase(UserDatabaseCassandra cassandra) {
        this.cassandra = cassandra;
    }

    public void init() {
        cassandra.init();
    }

    // must be unique
    public void insert(User user) {
        throw new UnsupportedOperationException();
    }

    // throw if not found
    public void delete(String id) {
        throw new UnsupportedOperationException();
    }

    // throw if not found
    public void update(User updated) {
        throw new UnsupportedOperationException();
    }

    public List<User> list() {
        throw new UnsupportedOperationException();
    }
}
