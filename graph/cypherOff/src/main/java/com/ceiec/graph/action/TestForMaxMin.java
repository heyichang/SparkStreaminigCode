package com.ceiec.graph.action;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

public class TestForMaxMin {
    public static void main(String[] args) {
        //获取连接
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //执行代码
        //执行语句
        StatementResult result = session.run("" +
                "match (n:n00030002) where n.influence is not null \n" +
                "with max(n.influence) as max ,min(n.influence) as min\n" +
                "match (n:n00030002) where n.influence is not null\n" +
                "set n.influence = (n.influence-min)/(max-min)");

        ResultSummary resultSummary = result.consume();

        //查看效果
        System.out.println("有多少节点数据更新:"+resultSummary.counters().propertiesSet());
    }
}
