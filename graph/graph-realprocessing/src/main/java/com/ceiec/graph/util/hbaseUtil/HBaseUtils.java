package com.ceiec.graph.util.hbaseUtil;


import com.ceiec.graph.util.InfoIdUtils;
import com.ceiec.graph.util.TimeUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author:heyichang
 * @deprecated:Hbase操作的基础工具类
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

    /**
     * 根据result打印出表中存储的值
     * @param result
     */
    public static void format(Result result) {

        for (Cell cell : result.rawCells()) {
            System.out.print(Bytes.toString(CellUtil.cloneRow(cell)) + "\t");  //得到rowkey
            System.out.print(Bytes.toString(CellUtil.cloneFamily(cell)) + "\t");//列族名
            System.out.print(Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t");     //字段名
            System.out.print(Bytes.toLong(CellUtil.cloneValue(cell)) + "\t");
            System.out.println();//值
        }
    }


    /**
     * 根据resultScanner打印出表中存储的值
     * @param resultScanner
     */
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

    /**
     * 获取rowkey,按照原始数据的起始节点和终止节点以及关系类型存储
     * @param startName 起始节点account name
     * @param endName 终止节点account name
     * @param name 关系名称
     * @return
     */
    public static  String getRowkeyOfBackUp(String startName,String endName,String name){
        //生成拼接字符窜
        StringBuffer sb = new StringBuffer();
        //生成rowkey字符串
        sb.append(startName).append("_").append(endName).append("_").append(name);
        //生成rowkey
        String rowKey = InfoIdUtils.generate32MD5ID(sb.toString());

        return rowKey;
    }

}
