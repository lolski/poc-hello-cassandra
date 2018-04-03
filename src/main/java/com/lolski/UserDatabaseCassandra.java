package com.lolski;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class UserDatabaseCassandra {
    private String host;
    private int port;
    private String keyspace;
    private String replicationStrategy;
    private int replicationFactor;
    private Cluster cluster;

    public UserDatabaseCassandra(String host, int port, String keyspace, String replicationStrategy, int replicationFactor) {
        this.host = host;
        this.port = port;
        this.keyspace = keyspace;
        this.replicationStrategy = replicationStrategy;
        this.replicationFactor = replicationFactor;
        cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
    }
    public void init() {
        Session session = getSession();
        createKeyspace(session, keyspace, replicationStrategy, replicationFactor);
    }

    public Session getSession() {
        return cluster.connect();
    }

    public void close() {
        cluster.close();
    }

    private void createKeyspace(Session session, String name, String replicationStrategy, int replicationFactor) {
        String query = "CREATE KEYSPACE " + name +
            " WITH replication = {'class': '" + replicationStrategy + "', 'replication_factor': " + replicationFactor + "};";

        session.execute(query);
    }
}
