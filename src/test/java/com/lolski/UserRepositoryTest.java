package com.lolski;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for simple App.
 */
public class UserRepositoryTest {
    private UserRepository underTest;

    @Before
    public void setup() {
        underTest = new UserRepositoryCassandra("localhost", 9042,
            randomKeyspaceName(),"SimpleStrategy", 1);
        underTest.init();
    }

    @Test
    public void insertTwoUsersWithIdenticalId_theOldOneShouldBePreserved() {
        String id1 = UUID.randomUUID().toString();

        final User user1 = User.create(id1, 27);
        final User user2 = User.create(id1, 25);

        underTest.insertIfNotExists(user1);
        underTest.insertIfNotExists(user2);

        Set<User> actual = underTest.list();
        assertThat(actual, equalTo(new HashSet<>(Arrays.asList(user1))));
    }

    // - test update a user
    @Test
    public void updateUser_shouldExecuteProperly() {
        User user1 = User.create(UUID.randomUUID().toString(), 1);
        User updatedUser1 = User.create(user1.getId(), 2);

        underTest.insertIfNotExists(user1);
        underTest.updateIfAgeIsNull(updatedUser1);

        Set<User> actual = underTest.list();

        assertThat(actual, equalTo(new HashSet<>(Arrays.asList(updatedUser1))));
    }

    // - test update a user when that particular user doesn't exist
    @Test
    public void updateNonExistingUser_shouldInsertIt() {
        User user1 = User.create(UUID.randomUUID().toString(), 1);

        underTest.update(user1);

        Set<User> actual = underTest.list();

        assertThat(actual, equalTo(new HashSet<>(Arrays.asList(user1))));
    }

    // - test conditional update a user when that particular user doesn't exist
    @Test
    public void updateWhenAgeIsNull_shouldNotInsertNonExistingUser() {
        User user1 = User.create(UUID.randomUUID().toString(), 1);

        underTest.updateIfAgeIsNull(user1);

        Set<User> actual = underTest.list();

        assertThat(actual, empty());
    }

    // - test delete a user
    @Test
    public void deleteUser_shouldExecuteProperly() {
        User user1 = User.create(UUID.randomUUID().toString(), 1);
        User user2 = User.create(UUID.randomUUID().toString(), 2);
        User user3 = User.create(UUID.randomUUID().toString(), 3);

        underTest.insertIfNotExists(user1);
        underTest.insertIfNotExists(user2);
        underTest.insertIfNotExists(user3);

        underTest.delete(user2.getId());

        Set<User> actual = underTest.list();

        assertThat(actual, equalTo(new HashSet<>(Arrays.asList(user1, user3))));
    }

    // - test delete a user, but user not found
    @Test
    public void deleteNonExistingUser_shouldExecuteProperly() {
        final User user1 = User.create(UUID.randomUUID().toString(), 1);
        final User user2 = User.create(UUID.randomUUID().toString(), 2);

        underTest.insertIfNotExists(user1);

        underTest.delete(user2.getId());

        Set<User> actual = underTest.list();

        assertThat(actual, equalTo(new HashSet<>(Arrays.asList(user1))));
    }

    @Test
    public void listUser_shouldReturnEmptyList_whenNoOneHasBeenRegistered() {
        assertThat(underTest.list(), empty());
    }

    @Test
    public void listUser_shouldReturnOneUser() {
        Set<User> expected = new HashSet<>(Arrays.asList(User.create(UUID.randomUUID().toString(), 25)));

        expected.forEach(underTest::insertIfNotExists);

        Set<User> actual = underTest.list();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void listUser_shouldReturnTwoUsers() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        Set<User> expected = new HashSet<>(Arrays.asList(User.create(id1, 25), User.create(id2, 27)));

        expected.forEach(underTest::insertIfNotExists);

        Set<User> actual = underTest.list();
        assertThat(actual, equalTo(expected));
    }

    private String randomKeyspaceName() {
        return "keyspace_" + UUID.randomUUID().toString().replace("-", "");
    }
}
