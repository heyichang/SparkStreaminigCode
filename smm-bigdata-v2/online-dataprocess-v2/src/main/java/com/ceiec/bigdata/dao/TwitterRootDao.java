package com.ceiec.bigdata.dao;


import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;

import java.sql.Connection;
import java.util.List;

/**
 * Created by heyichang on 2017/10/17.
 */
public interface TwitterRootDao {

     IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception;

     IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception;

     IndexRequestBuilder bulkAddTwitterUserData(Client client, String index, String type) throws Exception;

     IndexRequestBuilder updateEsVirtualData(Client client, String index, String type) ;

     UpdateRequest updateUserData( String index, String type) ;


     void keyWordWarning(Connection mysqlConn, List<String> eventForeList);

     void accoutAndLocationWarning(List<String> list, List<String> forelist, List<String> accountList, List<String> accountTimeList);


}
