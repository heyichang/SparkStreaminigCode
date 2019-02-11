package com.ceiec.graph.test.testSelect;

import com.ceiec.graph.dao.VirtualAccountDao;
import com.ceiec.graph.dao.impl.VirtualAccountDaoImpl;
import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.*;
import org.neo4j.graphdb.Relationship;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 15:08 2018-03-16
 */
public class Test {

    /**
     * 测试查询单个节点的一度关系
     */
    @org.junit.Test
    public void getRel(){
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        VirtualAccountDao virtualAccountDao = new VirtualAccountDaoImpl();

        Long startdriver = System.currentTimeMillis();
        System.out.println("begain:"+startdriver);
        Driver driver = neo4jDriver.driver;
        Long useTime = System.currentTimeMillis();
        System.out.println("open driver use time:"+(useTime-startdriver));
        try (Session session = driver.session()){
            Long startTime = System.currentTimeMillis();
            Transaction tx = session.beginTransaction();
                StatementResult statementResult =  tx.run("match (n:Account{nodeID:\"BDB8106C5276AE4AB67EF8FE9F7E4282\"})-[r:follow]->(n2:Account) return n,r,n2 limit 10");
                while (statementResult.hasNext()){
                    Record record = statementResult.next();
                    RelationshipValue relationshipValue = (RelationshipValue) record.get("r");
                    System.out.println(relationshipValue.asRelationship().type());
                    System.out.println("n:"+record.get("n")+" r:"+record.get("r")+" n2:"+record.get("n2"));
                }
            tx.success();
            Long endTime = System.currentTimeMillis();
            System.out.println("use Time:"+(endTime-startTime));
        }
        driver.close();
    }
}
