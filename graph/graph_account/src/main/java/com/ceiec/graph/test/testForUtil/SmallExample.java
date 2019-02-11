package com.ceiec.graph.test.testForUtil;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;

import java.util.Iterator;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 15:52 2018-03-05
 */
public class SmallExample {
    // Driver objects are thread-safe and are typically made available application-wide.
    Driver driver;

    public SmallExample(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    private void addPerson(String name)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
                tx.run("Merge (a:People {name: {x}})", parameters("x", name));
                tx.success();  // Mark this write as successful.
            }
        }
    }

    private void printPeople(String initial)
    {
        Session session = driver.session();

        // Auto-commit transactions are a quick and easy way to wrap a read.
        StatementResult result = session.run(
                "MATCH (a:People) WHERE a.name STARTS WITH {x} RETURN a.name as PeopleName  ",
                parameters("x", initial));
        // Each Cypher execution returns a stream of records.
        while (result.hasNext())
        {
            //Record 是一行记录，内容是什么取决于你return的东西
            Record record = result.next();
            System.out.println(record);
            // Values can be extracted from a record by index or name.
            System.out.println(record.get("PeopleName").asString());
        }

    }

    private void getPeoples()
    {
        Session session = driver.session();

        // Auto-commit transactions are a quick and easy way to wrap a read.
        StatementResult result = session.run(
                "MATCH (b:People) RETURN b");
        // Each Cypher execution returns a stream of records.
        while (result.hasNext())
        {
            //Record 是一行记录，内容是什么取决于你return的东西
            Record record = result.next();
            System.out.println(record);

            List<Value> list = record.values();
            for(Value v : list)
            {
                Node n = v.asNode();
                System.out.println(n.labels().iterator().next()+"--"+n.id());

                for(String k:n.keys())
                {
                    System.out.println(k+"---"+n.get(k) );
                }
                System.out.println("==========================");

            }

            // Values can be extracted from a record by index or name.
//            System.out.println(record.get("b").asString());
        }

    }

    private void getPeoplesAndRelation()
    {
        Session session = driver.session();

        // Auto-commit transactions are a quick and easy way to wrap a read.
        StatementResult result = session.run(
                "MATCH p=(b:People)-[]-(c) RETURN p");
//                "MATCH (b:People)-[]-(c) RETURN b,c");
        // Each Cypher execution returns a stream of records.
        while (result.hasNext())
        {
            //Record 是一行记录，内容是什么取决于你return的东西
            Record record = result.next();
            System.out.println(record);

            List<Value> list = record.values();
            for(Value v : list)
            {
                Path p = v.asPath();
                Node start = p.start();

                for(String k:start.keys())
                {
                    System.out.println(k+"---"+start.get(k) );
                }
                System.out.println("==========================");

                Iterator i = p.relationships().iterator();
                while(i.hasNext())
                {
//                    System.out.println(i.next()+"--------------------------");
                    Relationship r = (Relationship)i.next() ;
                    System.out.println(r.type());
                    System.out.println(r.startNodeId() + "->"+r.endNodeId());
                    System.out.println(r.id());

                }


                Node end = p.end();
                for(String k:end.keys())
                {
                    System.out.println(k+"---"+end.get(k) );
                }
                System.out.println("==========================");


//                Node n = v.asNode();
//                System.out.println(n.labels().iterator().next()+"--"+n.id());
//
//                for(String k:n.keys())
//                {
//                    System.out.println(k+"---"+n.get(k) );
//                }
//                System.out.println("==========================");

            }

            // Values can be extracted from a record by index or name.
//            System.out.println(record.get("b").asString());
        }

    }

    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }

    public static void main(String... args)
    {
        SmallExample example = new SmallExample("bolt://172.16.3.36:7687", "neo4j", "ceiec123456");
        example.addPerson("Ada");
        example.addPerson("Alice");
        example.addPerson("Bob");
        example.printPeople("A");
        example.getPeoplesAndRelation();
        example.close();
    }
}
