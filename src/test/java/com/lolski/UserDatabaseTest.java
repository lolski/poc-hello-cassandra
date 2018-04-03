package com.lolski;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for simple App.
 */
public class UserDatabaseTest {
    private UserDatabase underTest;

    @BeforeClass
    public static void setupOnce() throws InterruptedException, IOException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
    }

    @Before
    public void setup() {
        UserDatabaseCassandra cassandra = new UserDatabaseCassandra("localhost", 9042,
            randomKeyspaceName(),"SimpleStrategy", 1);
        underTest = new UserDatabase(cassandra);
        underTest.init();
    }

    @Test
    public void listUser_shouldReturnEmptyList_whenNoOneHasBeenRegistered() {
        assertThat(underTest.list(), empty());
    }
//
//    @Test
//    public void listUser_shouldReturnOneUser() {
//        List<User> expected = Arrays.asList(new User("1", 25));
//
//        expected.forEach(underTest::insert);
//
//        List<User> actual = underTest.list();
//        assertThat(expected, equalTo(actual));
//    }
//
//    @Test
//    public void listUser_shouldReturnTwoUsers() {
//        List<User> expected = Arrays.asList(new User("1", 25), new User("2", 27));
//
//        expected.forEach(underTest::insert);
//
//        List<User> actual = underTest.list();
//        assertThat(expected, equalTo(actual));
//    }

    // TODO:
    // - test insert two non-unique users

    // - test update a user
    // - test update a user, but user not found

    // - test delete a user
    // - test delete a user, but user not found

    private String randomKeyspaceName() {
        return "keyspace_" + UUID.randomUUID().toString().replace("-", "");
    }
}
