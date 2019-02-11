package com.ceiec.bigdata.dao.common;


import com.ceiec.bigdata.entity.table.EsVirtual;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.esutil.EsUtils;
import com.google.common.base.Preconditions;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by heyichang on 2017/11/8.
 */
public abstract class AbstractDaoImpl<T> implements BaseDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDaoImpl.class);

    private Class<T> clazz;

    private String className;

    protected Object obj;

    protected void setClazz(final Class<T> clazzToSet) {
        this.clazz = Preconditions.checkNotNull(clazzToSet);
        this.className = clazz.getSimpleName().toLowerCase();
    }

    public AbstractDaoImpl(Object clazz) {
        this.obj = clazz;
    }

    public AbstractDaoImpl() {
        this.clazz = clazz;
    }

    @Override
    public Put getHbaseRowPut() {

            try {
                Put put = new Put(Bytes.toBytes(InfoIdUtils.generate32MD5ID("")));
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    put.addColumn(Bytes.toBytes(Constants.examplesTableInfo.FAMILY), Bytes.toBytes(field.getName()), Bytes.toBytes((String) field.get(obj)));
                    String str = (String) field.get(obj);
                    System.out.println(field.getName() + " : " + str);
                }
                return put;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        return null;
    }

    public void batchAddHbaseRow(List<Put> putList, Table table){

        try {
            table.put(putList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        Field[] fields = clazz.getDeclaredFields();
        xContentBuilder.startObject();
        for (Field field : fields) {
            field.setAccessible(true);
            xContentBuilder.field(field.getName(), field.get(obj));
        }
        xContentBuilder.endObject();
        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, "");
    }

    @Override
    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception {
        EsVirtual esVirtual = new EsVirtual();
        esVirtual.getClass();
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        Field[] fields = clazz.getDeclaredFields();
        xContentBuilder.startObject();
        for (Field field : fields) {
            field.setAccessible(true);
            xContentBuilder.field(field.getName(), field.get(obj));
        }
        xContentBuilder.endObject();
        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, "");
    }



}
