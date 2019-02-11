package com.ceiec.graph.test;

import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * @author:heyichang
 * @description:主要测试java连接neo4j
 * @date:Created in 14:38 2018-03-02
 */
public class HelloWorld implements AutoCloseable{
    private final Driver driver;
    public HelloWorld( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting(final String message){
        Session session = driver.session();
        String greeting = session.writeTransaction(
                new TransactionWork<String>() {
                    @Override
                    public String execute(Transaction transaction) {
                        StatementResult result = transaction.run("CREATE (a:Greeting) " +
                                "SET a.message = $message " +
                                "RETURN a.message + ', from node ' + id(a)",parameters( "message", message ) );
                        return result.single().get(0).asString();
                    }
                }
        );
        System.out.println(greeting);
    }

    public static void main(String[] args) {
        try ( HelloWorld greeter = new HelloWorld( "bolt://172.16.3.36:7687", "neo4j", "ceiec123456" ) )
        {
            greeter.printGreeting( "hello, world" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
