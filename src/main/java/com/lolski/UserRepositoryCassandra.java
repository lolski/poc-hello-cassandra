package com.lolski;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.Set;
import java.util.stream.Collectors;

public class UserRepositoryCassandra implements UserRepository, AutoCloseable {
    public static final String TABLE_NAME = "user";
    private String host;
    private int port;
    private String keyspace;
    private String replicationStrategy;
    private int replicationFactor;
    private Cluster cluster;

    public UserRepositoryCassandra(String host, int port, String keyspace, String replicationStrategy, int replicationFactor) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
        this.replicationStrategy = replicationStrategy;
        this.replicationFactor = replicationFactor;
        cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
    }

    @Override
    public void init() {
        Session session = getSession();
        createKeyspace(session, keyspace, replicationStrategy, replicationFactor);
        createUserTable(session, keyspace, TABLE_NAME);
    }

    @Override
    public void insertIfNotExists(User user) {
        String query = "INSERT INTO " + keyspace + "." + TABLE_NAME + " (id, age) VALUES (" + user.getId() + ", " + user.getAge() + ") IF NOT EXISTS;";
        ResultSet a = getSession().execute(query);
        return;
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM " + keyspace + "." + TABLE_NAME + " WHERE id = " + id + ";";
        getSession().execute(query);
    }

    @Override
    public void updateIfAgeIsNull(User updated) {
        String query = "UPDATE " + keyspace + "." + TABLE_NAME + " SET age = " + updated.getAge() + " WHERE id = " + updated.getId() + " IF age != null;";
        getSession().execute(query);
    }

    @Override
    public Set<User> list() {
        String query = "SELECT * FROM " + keyspace + "." + TABLE_NAME + ";";
        ResultSet rows = getSession().execute(query);
        Set<User> users = rows.all().stream()
                .map(row -> User.create(row.getUUID("id").toString(), row.getInt("age")))
                .collect(Collectors.toSet());
        return users;
    }

    @Override
    public void close() {
        cluster.close();
    }

    private Session getSession() {
        return cluster.connect();
    }

    private void createKeyspace(Session session, String name, String replicationStrategy, int replicationFactor) {
        String query = "CREATE KEYSPACE " + name +
            " WITH replication = {'class': '" + replicationStrategy + "', 'replication_factor': " + replicationFactor + "};";

        session.execute(query);
    }

    private void createUserTable(Session session, String keyspace, String table) {
        String query = "CREATE TABLE " + keyspace + "." + table + "(id uuid PRIMARY KEY, age int);";
        session.execute(query);
    }
}
