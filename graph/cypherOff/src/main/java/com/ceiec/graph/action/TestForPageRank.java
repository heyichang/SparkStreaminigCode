package com.ceiec.graph.action;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

/**
 * @author heyichang
 * @description 测试cypher语句，pagerank
 */
public class TestForPageRank {
    public static void main(String[] args) {
        //获取连接
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //执行代码
        //执行语句
        StatementResult result = session.run("" +
                "CALL algo.pageRank(\"n00030002\", \"r00000001\",\n" +
                "    {iterations:20, dampingFactor:0.85, write: true, writeProperty:'influence', concurrency:4})\n" +
                "YIELD nodes, iterations, loadMillis, computeMillis, writeMillis, dampingFactor, write, writeProperty");

        ResultSummary resultSummary = result.consume();

        //查看效果
        System.out.println("有多少节点数据更新:"+resultSummary.counters().propertiesSet());
    }
}
