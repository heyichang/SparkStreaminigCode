package com.ceiec.bigdata.dao;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.sql.Connection;
import java.util.List;

/**
 * Created by heyichang on 2017/11/7.
 */
public interface NewsDao {

     Put getHbaseRowPut();

     void batchAddHbaseRow(List<Put> put_list, Table table) ;

     IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception;

     IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception;

     void keyWordWarning(Connection mysqlConn, List<String> eventForeList);


}
