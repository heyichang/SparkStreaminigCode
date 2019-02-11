package com.ceiec.graph.test;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.junit.Test;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:heyichang
 * @description:
 * @date:Created in 14:22 2018-03-14
 */
public class TestForBulk04 {
    @Test
    public void shouldBeAbleToUnwindEmptyListsWithoutFailure() {
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //Parameters，可以设置多个batches
        Map<String, Object> parameters = new HashMap<>();

        //batches，批量配置一些变量
        List<Map<String, Object>> batches = new ArrayList<>();

        //循环生成Batch数据
        for (int i = 1; i < 10; ++i) {
            batches.add(createBatch(i));
        }

        parameters.put("batches", batches);

        /**
         * 获取批量数据
         * 生成Node 节点，并设置属性
         * 将a batch 作为变量，转为下一个unwind
         *  获取 prev 参数 多个 map对象
         *  生成 Person
         */
        StatementResult result = session.run(
                "UNWIND {batches} as batch " +
                        "CREATE (a:Node) SET a.id = batch.id, a.login = batch.login " +
                        "WITH a, batch " +
                        "UNWIND batch.prev as prev " +
                        "MERGE (o:Person {id: prev.id})" +
                        "MERGE (a)-[:RELATES]->(o)", parameters
        );
        result.consume();
    }

    /**
     * batch id login prev
     * @param i
     * @return
     */
    private Map<String, Object> createBatch(int i) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", i);
        map.put("login", "login" + i);
        List<Map<String, Object>> prev = i > 6 ? createPrev() : new ArrayList<Map<String, Object>>();
        map.put("prev", prev);

        return map;
    }

    /**
     * 生成prev数据
     * 多个map对象
     * @return
     */
    private List<Map<String, Object>> createPrev() {
        List<Map<String, Object>> prevs = new ArrayList<>();
        for (int i = 1; i < 6; ++i) {
            Map<String, Object> prev = new HashMap<>();
            prev.put("id", i);
            prev.put("second", ++i);
            prevs.add(prev);
        }

        return prevs;
    }
}
