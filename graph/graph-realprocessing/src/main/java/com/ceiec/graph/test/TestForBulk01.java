package com.ceiec.graph.test;

import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 10:58 2018-03-08
 */
public class TestForBulk01 {
    private Driver driver;

    public TestForBulk01(Driver driver) {
        this.driver = driver;
    }

    // Create a company node
    private StatementResult addCompany(final Transaction tx, final String name )
    {
        return tx.run( "CREATE (:Company {name: $name})", parameters( "name", name ) );
    }

    // Create a person node
    private StatementResult addPerson( final Transaction tx, final String name )
    {
        return tx.run( "CREATE (:Person {name: $name})", parameters( "name", name ) );
    }

    // Create an employment relationship to a pre-existing company node.
// This relies on the person first having been created.
    private StatementResult employ( final Transaction tx, final String person, final String company )
    {
        return tx.run( "MATCH (person:Person {name: $person_name}) " +
                        "MATCH (company:Company {name: $company_name}) " +
                        "CREATE (person)-[:WORKS_FOR]->(company)",
                parameters( "person_name", person, "company_name", company ) );
    }

    // Create a friendship between two people.
    private StatementResult makeFriends( final Transaction tx, final String person1, final String person2 )
    {
        return tx.run( "MATCH (a:Person {name: $person_1}) " +
                        "MATCH (b:Person {name: $person_2}) " +
                        "MERGE (a)-[:KNOWS]->(b)",
                parameters( "person_1", person1, "person_2", person2 ) );
    }

    // Match and display all friendships.
    private StatementResult printFriends( final Transaction tx )
    {
        StatementResult result = tx.run( "MATCH (a)-[:KNOWS]->(b) RETURN a.name, b.name" );
        while ( result.hasNext() )
        {
            Record record = result.next();
            System.out.println( String.format( "%s knows %s", record.get( "a.name" ).asString(), record.get( "b.name" ).toString() ) );
        }
        return result;
    }

    public void addEmployAndMakeFriends()
    {
        // To collect the session bookmarks
        List<String> savedBookmarks = new ArrayList<>();

        // Create the first person and employment relationship.
        try ( Session session1 = driver.session( AccessMode.WRITE ) )
        {
            session1.writeTransaction( tx -> addCompany( tx, "Wayne Enterprises" ) );
            session1.writeTransaction( tx -> addPerson( tx, "Alice" ) );
            session1.writeTransaction( tx -> employ( tx, "Alice", "Wayne Enterprises" ) );

            savedBookmarks.add( session1.lastBookmark() );
        }

        // Create the second person and employment relationship.
        try ( Session session2 = driver.session( AccessMode.WRITE ) )
        {
            session2.writeTransaction( tx -> addCompany( tx, "LexCorp" ) );
            session2.writeTransaction( tx -> addPerson( tx, "Bob" ) );
            session2.writeTransaction( tx -> employ( tx, "Bob", "LexCorp" ) );

            savedBookmarks.add( session2.lastBookmark() );
        }

        // Create a friendship between the two people created above.
        try ( Session session3 = driver.session( AccessMode.WRITE, savedBookmarks ) )
        {
            session3.writeTransaction( tx -> makeFriends( tx, "Alice", "Bob" ) );

            session3.readTransaction( this::printFriends );
        }
    }
}
