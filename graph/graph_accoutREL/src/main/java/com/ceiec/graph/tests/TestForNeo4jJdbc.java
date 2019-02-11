package com.ceiec.graph.tests;


import org.neo4j.jdbc.Connection;
import org.neo4j.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TestForNeo4jJdbc {
    public static void main(String[] args) throws SQLException {
        Connection neoConn = (Connection) DriverManager.getConnection("jdbc:neo4j:bolt://172.16.3.31:7687");

        // Querying
        String query = "MATCH (u:User)-[:FRIEND]-(f:User) WHERE u.name = {1} RETURN f.name, f.age";
        String query2 = "create (n:test) set n.name = 'hyc',n.age = 18";
        PreparedStatement stmt = (PreparedStatement) neoConn.prepareStatement(query2);
        stmt.execute();
//        neoConn.commit();
//        try (PreparedStatement stmt = (PreparedStatement) neoConn.prepareStatement(query)) {
//            stmt.setString(1,"John");
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    System.out.println("Friend: "+rs.getString("f.name")+" is "+rs.getInt("f.age"));
//                }
//            }
//        }

    }

}
