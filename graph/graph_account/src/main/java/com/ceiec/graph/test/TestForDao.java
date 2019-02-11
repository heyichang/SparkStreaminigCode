package com.ceiec.graph.test;

import com.ceiec.graph.dao.VirtualAccountDao;
import com.ceiec.graph.dao.impl.VirtualAccountDaoImpl;
import com.ceiec.graph.util.neo4jUtil.Neo4jDriver;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

/**
 * @author:heyichang
 * @description:测试dao层方法
 * @date:Created in 18:04 2018-03-05
 */
public class TestForDao {
    public static void main(String[] args) {
        Neo4jDriver neo4jDriver = new Neo4jDriver();
        VirtualAccountDao virtualAccountDao = new VirtualAccountDaoImpl();
//        virtualAccountDao.insertVirtualAccount("abcdefg","heyichangtest",neo4jDriver.driver);
//       virtualAccountDao.insertRelationshop("abcdefg","abcdefg2","fans", neo4jDriver.driver);
//        virtualAccountDao.deleteNode("abcdefg",neo4jDriver.driver);
//        virtualAccountDao.deleteRelationship("abcdefg","abcdefg2","fans",neo4jDriver.driver);
//        virtualAccountDao.setPropertiesOfNode("abcdefg","test","aaaaa",neo4jDriver.driver);
//        virtualAccountDao.setPropertiesOfRelationship("abcdefg","abcdefg2","fans","test","aaaaa",neo4jDriver.driver);
//        virtualAccountDao.removePropertiesOfNote("abcdefg","test",neo4jDriver.driver);
//        virtualAccountDao.removePropertiesOfRelatoinship("abcdefg","abcdefg2","fans","test",neo4jDriver.driver);

        Long startdriver = System.currentTimeMillis();
        System.out.println("begain:"+startdriver);
        Driver driver = neo4jDriver.driver;
        Long useTime = System.currentTimeMillis();
        System.out.println("open driver use time:"+(useTime-startdriver));
        try (Session session = driver.session()){
            Long startTime = System.currentTimeMillis();
            Transaction tx = session.beginTransaction();
            for(int i=160000;i<200000;i++){
                virtualAccountDao.insertVirtualAccount("abcdefg"+i,"heyichangtest"+i,tx);
                if (i%10000 == 0){
                    System.out.println("现在插入 节点:"+i+"时间:"+System.currentTimeMillis());
                }
            }
            tx.success();
            Long endTime = System.currentTimeMillis();
            System.out.println("use Time:"+(endTime-startTime));
        }
        driver.close();
    }
}
