package com.ceiec.bigdata.action;

import com.ceiec.bigdata.util.esutil.EsClient;
import com.ceiec.bigdata.util.esutil.EsUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by heyichang on 2018/3/26.
 */
public class UpdateCNNRegion {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        TransportClient esClient = EsClient.getInstance().getTransportClient();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("account_id","D7447C9CE9EC68E4E900D0ABAF91D8A8"));
        boolQueryBuilder.must(QueryBuilders.rangeQuery("gather_time").gt("2018-03-27 00:00:00").lte("2018-03-27 10:00:00"));
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch("m_es_info").setTypes("info");
        searchRequestBuilder.setQuery(boolQueryBuilder).setFetchSource(new String[]{"info_id"},null);
        searchRequestBuilder.setScroll(new TimeValue(30000)).setSize(1000);
        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        BulkRequestBuilder bulkRequestBuilder = esClient.prepareBulk();
        for (SearchHit searchHit: searchHits) {

            String infoID = (String) searchHit.getSourceAsMap().get("info_id");
            System.out.println(infoID);
            UpdateRequest updateRequest = EsUtils.updateLocation("m_es_info","info",infoID);
            bulkRequestBuilder.add(updateRequest);
        }
        if(bulkRequestBuilder.numberOfActions() > 0){
            bulkRequestBuilder.execute().actionGet();
        }

        esClient.close();

    }
}
