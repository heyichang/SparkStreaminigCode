package com.ceiec.graph.dao;


import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import java.util.Map;

/**
 * @author:heyichang
 * @description:虚拟账号节点Dao层
 * @date:Created in 17:09 2018-03-05
 */
public interface VirtualAccountDao {

    /**
     * 新增节点
     * @param accountId
     * @param name
     */
    public void insertVirtualAccount(String accountId, String name, Transaction tx);

    /**
     * 新增关系
     * @param startAccountId
     * @param endAccountId
     * @param labelName
     */
    public void insertRelationshop(String startAccountId,String endAccountId,String labelName,Transaction tx);

    /**
     * 删除节点
     * @param accountId
     */
    public void deleteNode(String accountId,Transaction tx);

    /**
     * 删除边
     * @param startAccountId
     * @param endAccountId
     * @param labelName
     */
    public void deleteRelationship(String startAccountId,String endAccountId,String labelName,Transaction tx);

    /**
     * 新增属性信息和修改属性
     * @param accountId
     * @param property
     */
    public void setPropertiesOfNode(String accountId, String key,String property,Transaction tx);

    public void setPropertiesOfNode(String accountId, String key,int property,Transaction tx);

    /**
     * 新增属性信息和修改属性
     * @param startAccountID
     * @param endAccountId
     * @param labelName
     * @param property
     */
    public void setPropertiesOfRelationship(String startAccountID,String endAccountId,String labelName,String key,String property,Transaction tx);

    public void setPropertiesOfRelationship(String startAccountID,String endAccountId,String labelName,String key,int property,Transaction tx);

    /**
     * 删除属性
     * @param accountId
     * @param propertyName
     */
    public void removePropertiesOfNote(String accountId,String propertyName,Transaction tx);

    /**
     * 删除属性
     * @param startAccountID
     * @param endAccountId
     * @param labelName
     * @param propertyName
     */
    public void removePropertiesOfRelatoinship(String startAccountID,String endAccountId,String labelName,String propertyName,Transaction tx);
}
