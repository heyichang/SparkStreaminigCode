package com.ceiec.graph.tests;

import com.ceiec.graph.utils.neo4jUtil.Neo4jDriver;
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
 * @date:Created in 15:11 2018-03-14
 */
public class TestForBulk05 {

    public static void main(String[] args) {
        System.out.println("启动");
        testForNode();
        System.out.println("结束");
    }
    /**
     * 测试批量插入节点数据
     */
//    @Test
    public static void testForNode(){

        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //参数变量
        Map<String, Object> parameters = new HashMap<>();

        Long startTime = System.currentTimeMillis();

        /**
         * 设置节点
         */
        //batches 变量
        List batches = new ArrayList();

        for (int i=0;i<10000;i++){
            Map batch = new HashMap();
            batch.put("accountId","accountId"+i);
            Map prev = new HashMap();
            prev.put("name","Alice"+i);
            prev.put("age",i);
            batch.put("prev",prev);
            batches.add(batch);
        }

        //设置变量
        parameters.put("batches", batches);

        //执行语句
        StatementResult result = session.run("" +
                "UNWIND {batches} as row\n" +
                "merge (n:Account {accountId: row.accountId})\n" +
                "SET n.name = row.prev.name,n.age = row.prev.age",parameters);

        ResultSummary resultSummary = result.consume();

        Long endTime = System.currentTimeMillis();

        System.out.println(resultSummary.counters());

        System.out.println("use time :"+(endTime-startTime));
    }

    /**
     * 插入测试数据
     */
//    @Test
    public static void testData(){
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //参数变量
        Map<String, Object> parameters = new HashMap<>();

        Long startTime = System.currentTimeMillis();

        /**
         * 设置节点
         */
        //batches 变量
        List batches = new ArrayList();

        for (int i=0;i<20000;i++){
            Map batch = new HashMap();
            batch.put("accountId","accountId"+i);
            batch.put("name","Alice"+i);
            batch.put("age",i);
            batches.add(batch);
        }

        //设置变量
        parameters.put("batches", batches);

        //执行语句
        StatementResult result = session.run("" +
                "UNWIND {batches} as row\n" +
                "merge (n:Account {accountId: row.accountId})\n" +
                "SET n.name = row.name,n.age = row.age",parameters);

        ResultSummary resultSummary = result.consume();

        Long endTime = System.currentTimeMillis();

        System.out.println(resultSummary.counters());

        System.out.println("use time :"+(endTime-startTime));

    }
}
