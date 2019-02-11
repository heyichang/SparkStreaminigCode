package com.ceiec.graph.action;

import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

public class MainTwitterPageRank {
    public static void main(String[] args) {
        //获取连接
        //获取session
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        Driver driver = neo4jDriver.driver;
        Session session = driver.session();

        //执行代码
        //执行语句
        StatementResult resultFP = session.run("" +
                "CALL algo.pageRank(\"n00030001\", \"r00000008\",\n" +
                "    {iterations:20, dampingFactor:0.85, write: true, writeProperty:'influence', concurrency:4})\n" +
                "YIELD nodes, iterations, loadMillis, computeMillis, writeMillis, dampingFactor, write, writeProperty");

        ResultSummary resultFPSummary = resultFP.consume();

        StatementResult resultFMM = session.run("" +
                "match (n:n00030001) where n.influence is not null \n" +
                "with max(n.influence) as max ,min(n.influence) as min\n" +
                "match (n:n00030001) where n.influence is not null\n" +
                "set n.influence = (n.influence-min)/(max-min)");

        ResultSummary resultFMMSummary = resultFMM.consume();
    }
}
