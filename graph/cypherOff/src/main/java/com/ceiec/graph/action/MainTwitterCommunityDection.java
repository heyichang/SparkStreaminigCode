package com.ceiec.graph.action;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

public class MainTwitterCommunityDection {
    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        //获取连接
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //执行代码
        //执行语句
        StatementResult result = session.run("" +
                "CALL apoc.algo.community(25,['n00030001'],'community','r00000008',null,'weight',10000)");

        ResultSummary resultSummary = result.consume();

        //查看效果
        System.out.println("有多少节点数据更新:"+resultSummary.counters().propertiesSet());

        long endTime = System.currentTimeMillis();

        System.out.println("use time:"+(endTime-beginTime));
    }
}
