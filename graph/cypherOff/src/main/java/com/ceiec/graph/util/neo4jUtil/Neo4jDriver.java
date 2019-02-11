package com.ceiec.graph.util.neo4jUtil;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

/**
 * @author:heyichang
 * @description:获取neo4j drive
 * @date:Created in 17:06 2018-03-05
 */
public class Neo4jDriver {

    //neo4jDriver 单例
    private static Neo4jDriver neo4jDriver;

    public static Neo4jDriver getSingleton() {
        if (neo4jDriver == null) {
            synchronized (Neo4jDriver.class) {
                if (neo4jDriver == null) {
                    neo4jDriver = new Neo4jDriver();
                }
            }
        }
        return neo4jDriver;
    }

    //获取Driver
    public  Driver driver;

    public Neo4jDriver() {
        driver = GraphDatabase.driver(Neo4jConstants.NEO4J_URI, AuthTokens.basic(Neo4jConstants.NEO4J_USER, Neo4jConstants.NEO4J_PASSWORD));
    }

    public  void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }
}
