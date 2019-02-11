package com.ceiec.graph.utils.esUtil;


//import com.ceiec.graph.entity.table.EsVirtual;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Created by zoumengcheng on 2017/10/19.
 */
public  class EsUtils {


    private static final Logger logger = LoggerFactory.getLogger(EsUtils.class);

    /**
     *       导入单条数据
     * @param client
     * @param xContentBuilder
     */
    public static void saveData(Client client,XContentBuilder xContentBuilder,String index,String type,String id) {

        try {
            IndexResponse response = client.prepareIndex(index, type, id.toString())
                    .setSource(xContentBuilder).execute().actionGet();
           // System.out.println("写入数据结果=" + response.status().getStatus() + "！id=" + response.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //client.close();
        }
    }

    /**
     *        导入单条数据
     * @param client  连接
     * @param map   单条数据的map集合
     * @param index 索引
     * @param type 类型
     * @param id  唯一
     */
    public  static void saveData(Client client,Map<String,Object> map,String index,String type,String id) {

        try {
            IndexResponse response = client.prepareIndex(index, type, id.toString())
                    .setSource(map).execute().actionGet();
            response.status();


           // System.out.println("写入数据结果=" + response.status().getStatus() + "！id=" + response.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //client.close();
        }
    }


    /**
     *   批量导入数据的IndexRequestBuilder
     * @param client
     * @param map
     * @param index
     * @param type
     * @param id
     * @return IndexRequestBuilder
     */
    public static  IndexRequestBuilder  bulkCreateData(Client client,Map<String,Object> map,String index,String type,String id) {
        try {
        IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type)
                .setSource(map).setId(id);
        return indexRequestBuilder;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //client.close();
        }
        return null;
    }

    /**
     *   批量导入数据的IndexRequestBuilder
     * @param client
     * @param xContentBuilder
     * @param index
     * @param type
     * @param id
     * @return IndexRequestBuilder
     */
    public static  IndexRequestBuilder  bulkCreateData(Client client,XContentBuilder xContentBuilder,String index,String type,String id) {
        try {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type)
                    .setSource(xContentBuilder).setId(id);
            return indexRequestBuilder;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //client.close();
        }
        return null;
    }

    /**
     *
     * @param client
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static Boolean isExistsRetweetedId(Client client,String index, String type, String id) {
        boolean flag = false;
        GetResponse getResponse = client.prepareGet(index, type, id).get();
        if(getResponse.isExists()){
            flag = true;
        }
        return flag;
    }

//    /**
//     * 更新EsVirtual数据
//     * @param client
//     * @param index
//     * @param type
//     * @param id
//     * @param esVirtual
//     * @return
//     * @throws IOException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public static UpdateRequest updateEsVirtual(Client client, String index, String type, String id, EsVirtual esVirtual) throws IOException, ExecutionException, InterruptedException {
//
//
//        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
//                .doc(XContentFactory.jsonBuilder()
//                        .startObject()
//                        .endObject());
//        //client.update(updateRequest).get();
//        return updateRequest;
//    }


}
