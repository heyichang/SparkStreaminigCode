package com.ceiec.bigdata.util.hbaseutil;

import com.ceiec.bigdata.util.TimeUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by heyichang on 2017/10/26.
 */
public class HBaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HBaseUtils.class);


    /**
     * 添加单条数据
     * @param rowkey
     * @param columnFamily
     * @param column
     * @param value
     * @throws Exception
     */
    public static Put addRow( String rowkey, String columnFamily, String column, String value) {
        //创建连接
        Put put = new Put(Bytes.toBytes(rowkey));// 指定行
        // 参数分别:列族、列、值
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        return put;
    }

    /**
     * 批量添加数据
     *
     * @param table
     * @param puts
     */
    public static void batchAddPut(Table table, List<Put> puts) {
        try {
            table.put(puts);
        } catch (IOException e) {
            try {
                table.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            logger.error("hbase batchput error");
            e.printStackTrace();
        }
    }

    /**
     * 通过rowkey拿到行值
     *
     * @param table
     * @param rowkey
     * @return
     */
    public static Result getDataByRowkey(Table table, String rowkey) {
        try {

            Get get = new Get(rowkey.getBytes());
            return table.get(get);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;

    }

    public static Increment addIncrement(String taskid, String cf) {

        String[] strings = TimeUtils.getTimeForHbase().split(" ");
        String rowKey;
        if (taskid != null) {
            rowKey = taskid + "_" + strings[0];
        } else {
            rowKey = strings[0];
        }

        String hourColum = strings[1];
        Increment increment = new Increment(Bytes.toBytes(rowKey));
        increment.addColumn(cf.getBytes(), hourColum.getBytes(), 1L);
        return increment;
    }


    public static void batchAddIncrement(Table table, List<Increment> list) throws IOException, InterruptedException {
        Object[] results = new Object[list.size()];
        table.batch(list, results);
    }


    //输出result结果
    public static void format(Result result) {

        for (Cell cell : result.rawCells()) {
            System.out.print(Bytes.toString(CellUtil.cloneRow(cell)) + "\t");  //得到rowkey
            System.out.print(Bytes.toString(CellUtil.cloneFamily(cell)) + "\t");//列族名
            System.out.print(Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t");     //字段名
            System.out.print(Bytes.toLong(CellUtil.cloneValue(cell)) + "\t");
            System.out.println();//值
        }
    }


    //输出result结果
    public static void formatScan(ResultScanner resultScanner) {

        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                System.out.print(Bytes.toString(CellUtil.cloneRow(cell)) + "\t");  //得到rowkey
                System.out.print(Bytes.toString(CellUtil.cloneFamily(cell)) + "\t");//列族名
                System.out.print(Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t");     //字段名
                System.out.print(Bytes.toLong(CellUtil.cloneValue(cell)) + "\t");
                System.out.println();//值
            }
        }

    }


    public static void main(String[] args) throws IOException {
//        deleteTable("result:statistic");
//        createTableWithSnappyCompress("result:statistic");
    }
}
