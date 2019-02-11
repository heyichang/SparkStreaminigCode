package com.ceiec.graph.test;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 11:26 2018-03-08
 */
public class TestBulk02 {
    public static void main(String[] args) {
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();
        Long startTime = System.currentTimeMillis();
        //参数变量
        Map<String, Object> parameters = new HashMap<>();

        /**
         * 设置节点
         */
        //batches 变量
        List batches = new ArrayList();

        for (int i=300000;i<310000;i++){
            Map batch = new HashMap();
            batch.put("name","Alice"+i);
            batch.put("age",i);
            batches.add(batch);
        }

        //设置变量
        parameters.put("batches", batches);
        //执行语句
        StatementResult result = session.run(
                "UNWIND {batches} as row\n" +
                        "create (n:Account)\n" +
                        "SET n.name = row.name, n.age = row.age", parameters
        );

        ResultSummary resultSummary = result.consume();

        Long endTime = System.currentTimeMillis();

        System.out.println(resultSummary.counters());

        System.out.println("use time :"+(endTime-startTime));
    }
}
