package com.ceiec.graph.dao.impl;

import com.ceiec.graph.dao.VirtualAccountDao;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;


import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * @author:heyichang
 * @description:虚拟账号节点实现类
 * @date:Created in 17:41 2018-03-05
 */
public class VirtualAccountDaoImpl implements VirtualAccountDao {
    @Override
    public void insertVirtualAccount(String accountId, String name, Transaction tx) {
                tx.run("Merge (a:VirtualAccount {accountID: {x},name:{y}})", parameters("x", accountId,"y",name));
    }

    @Override
    public void insertRelationshop(String startAccountId, String endAccountId, String labelName, Transaction tx) {
                tx.run("match(a:VirtualAccount{accountID:{x}}),(b:VirtualAccount{accountID:{y}})\n" +
                        "merge (a)-[r:"+labelName+"]->(b)", parameters("x", startAccountId,"y",endAccountId));
    }

    @Override
    public void deleteNode(String accountId, Transaction tx) {
                tx.run("        match(p:VirtualAccount{accountID:{x}}),(p)-[r]-(a)\n" +
                        "                delete p,r", parameters("x", accountId));
    }

    @Override
    public void deleteRelationship(String startAccountId, String endAccountId, String labelName,Transaction tx) {
                tx.run("match (a:VirtualAccount{accountID:{x}})-[r:"+labelName+"]->(b:VirtualAccount{accountID:{y}})\n" +
                        "delete r", parameters("x", startAccountId,"y",endAccountId));
    }

    @Override
    public void setPropertiesOfNode(String accountId, String key, String property,Transaction tx) {
                tx.run("match (p:VirtualAccount{accountID:{x}})\n" +
                        "set p."+key+"={y}", parameters("x", accountId,"y",property));
    }

    @Override
    public void setPropertiesOfNode(String accountId, String key, int property,Transaction tx) {
                tx.run("match (p:VirtualAccount{accountID:{x}})\n" +
                        "set p."+key+"={y}", parameters("x", accountId,"y",property));
    }

    @Override
    public void setPropertiesOfRelationship(String startAccountID, String endAccountId, String labelName, String key, String property, Transaction tx) {
                tx.run("match (a:VirtualAccount{accountID:{x}})-[r:"+labelName+"]-(b:VirtualAccount{accountID:{y}})\n" +
                        "set r."+key+" = {z}", parameters("x", startAccountID,"y",endAccountId,"z",property));
    }

    @Override
    public void setPropertiesOfRelationship(String startAccountID, String endAccountId, String labelName, String key, int property,Transaction tx) {
                tx.run("match (a:VirtualAccount{accountID:{x}})-[r:"+labelName+"]-(b:VirtualAccount{accountID:{y}})\n" +
                        "set r."+key+" = {z}", parameters("x", startAccountID,"y",endAccountId,property));
    }


    @Override
    public void removePropertiesOfNote(String accountId, String propertyName,Transaction tx) {
                tx.run("match (p:VirtualAccount{accountID:{x}})\n" +
                        "remove p."+propertyName, parameters("x", accountId));
    }

    @Override
    public void removePropertiesOfRelatoinship(String startAccountID, String endAccountId, String labelName, String propertyName,Transaction tx) {
                tx.run("match (a:VirtualAccount{accountID:{x}})-[r:"+labelName+"]-(b:VirtualAccount{accountID:{y}})\n" +
                        "remove r."+propertyName, parameters("x", startAccountID,"y",endAccountId));
    }
}
