package com.ceiec.graph.utils.esUtil;



import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author heyichang
 * @created 18/05/28
 * @deprecated get client
 */
public class EsClient {

    private static final Logger logger = LoggerFactory.getLogger(EsClient.class);

    private static volatile  EsClient instance = null;

    private static TransportClient transportClient;

    static {
        System.setProperty(EsConstants.ES_SET_NETTY,EsConstants.ES_SET_NETTY_FALSE);
        Settings settings = Settings.builder().put(EsConstants.CLUSTER_NAME, EsConstants.MY_CLUSTER_NAME)
                .put(EsConstants.TRANSPORT_TYPE,EsConstants.NETTY3)
                .put(EsConstants.HTTP_TYPE, EsConstants.NETTY3)
                .build();

        try {
            transportClient = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_1), EsConstants.PORT))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_2), EsConstants.PORT))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_3), EsConstants.PORT))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_4), EsConstants.PORT))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_5), EsConstants.PORT))
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.HOST_6), EsConstants.PORT))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(EsConstants.Local_Host), EsConstants.PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private EsClient(){

    }

    public static EsClient getInstance() {
        if (instance == null) {
            synchronized (EsClient.class) {
                if (instance == null) {
                    instance = new EsClient();
                }
            }
        }
        return instance;
    }



    public TransportClient getTransportClient() {
        return transportClient;
    }

    public void setTransportClient(TransportClient transportClient) {
        this.transportClient = transportClient;
    }
}
