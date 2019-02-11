package com.ceiec.graph.dao;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;

/**
 * @author:heyichang
 * @description:针对Person新增和删除修改操作
 * @date:Created in 21:17 2018-03-05
 */
public interface PersonDao {

    /**
     * 新增节点
     * @param name
     * @param Age
     * @param professional
     * @param national
     */
    public void insertNode(String name,String Age,String professional,String national,Session session);

    /**
     * 新增关系
     * @param startName
     * @param endName
     * @param labelName
     * @param session
     */
    public void insertRelationshop(String startName,String endName,String labelName,Session session);


}
