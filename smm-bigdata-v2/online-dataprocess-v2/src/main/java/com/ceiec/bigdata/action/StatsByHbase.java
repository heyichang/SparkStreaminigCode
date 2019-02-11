package com.ceiec.bigdata.action;

import com.ceiec.bigdata.util.hbaseutil.HBaseConnectionFactory;
import com.ceiec.bigdata.util.hbaseutil.HBaseUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * Created by heyichang on 2017/12/27.
 */
public class StatsByHbase {
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = HBaseConnectionFactory.getConnection();
//        List<Increment> list = new ArrayList<>();
        Table table = connection.getTable(TableName.valueOf("crawled_stats"));
//        for (int i =0;i<100;i++) {
//
//            Increment increment = new Increment(Bytes.toBytes("taskid_2017-12-27"));
//            increment.addColumn("by_hour".getBytes(), "click_count_hour".getBytes(), 1L);
//            list.add(increment);
//        }
//        Object[] results = new Object[list.size()];
//        table.batch(list,results);

//        Get get=new Get("1fe92b2f8f0647bc96829314788d4815_2017-12-29".getBytes());
        Scan scan = new Scan();
        ResultScanner result = table.getScanner(scan);
        HBaseUtils.formatScan(result);

//        System.out.println(get);
    }
}

