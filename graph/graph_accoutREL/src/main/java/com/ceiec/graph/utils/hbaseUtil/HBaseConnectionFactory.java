package com.ceiec.graph.utils.hbaseUtil;



import com.ceiec.graph.utils.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author:heyichang
 * @deprecated:Hbase连接
 * @date:2018-04-04
 */
public class HBaseConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnectionFactory.class);

    //Hbase连接配置
    private static Configuration config;

    //Hbase连接
    private static Connection connection = null;

    static {
        try {

            config = HBaseConfiguration.create();
            config.set(HConstants.ZOOKEEPER_QUORUM, Constants.ZK_LIST);
            config.set(Constants.ZOOKEEPER_ZNODE_PARENT,Constants.PARENT_DIR);
            config.setInt(HConstants.HBASE_CLIENT_SCANNER_CACHING, 10000); // 客户端扫描仪缓存
            config.setInt(HConstants.HBASE_CLIENT_RETRIES_NUMBER, 128);
            config.setLong(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD, 10 * 60 * 1000L);
            config.setLong(HConstants.HBASE_RPC_TIMEOUT_KEY, 10 * 60 * 1000L);

            connection = ConnectionFactory.createConnection(config);
        } catch (IOException e) {
            logger.error("IOException", e);
            System.exit(1);
        }
    }

    public static Connection getConnection() throws IOException {
        return connection;
    }

    public static void reconnect() throws IOException {
        try {
            connection.close();
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        connection = ConnectionFactory.createConnection(config);
    }

    public static void close() throws IOException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
