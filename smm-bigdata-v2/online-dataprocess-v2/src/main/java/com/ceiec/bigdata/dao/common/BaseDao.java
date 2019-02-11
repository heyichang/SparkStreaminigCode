package com.ceiec.bigdata.dao.common;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.util.List;

/**
 * Created by heyichang on 2017/11/8.
 */
public interface BaseDao<T> {

    public Put getHbaseRowPut();

    public void batchAddHbaseRow(List<Put> put_list, Table table) ;

    public IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception;

    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception;

}
