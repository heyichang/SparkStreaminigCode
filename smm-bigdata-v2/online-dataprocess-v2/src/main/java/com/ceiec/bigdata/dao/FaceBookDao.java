package com.ceiec.bigdata.dao;

import com.ceiec.bigdata.entity.facebook.es.FaceBookUserCommon;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.sql.Connection;
import java.util.List;

/**
 * Created by heyichang on 2018/2/9.
 */
public interface FaceBookDao {
    IndexRequestBuilder bulkAddPostEsData(Client client, String index, String type) throws Exception;

    IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception;

    IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception;

    IndexRequestBuilder bulkAddEsUserData(Client client, String index, String type) throws Exception;

    IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type, FaceBookUserCommon faceBookUserCommon) throws Exception;

    IndexRequestBuilder updateEsVirtualData(Client client, String index, String type) ;

    void keyWordWarning(Connection mysqlConn, List<String> eventForeList);

    void accoutAndLocationWarning(List<String> list, List<String> forelist, List<String> accountList, List<String> accountTimeList);

}
