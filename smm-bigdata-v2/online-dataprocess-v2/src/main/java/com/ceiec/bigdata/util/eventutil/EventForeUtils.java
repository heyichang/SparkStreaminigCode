package com.ceiec.bigdata.util.eventutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyichang on 2017/12/25.
 */
public class EventForeUtils {
    private static final Logger logger = LoggerFactory.getLogger(EventForeUtils.class);

    // 定义数据库的链接
    private Connection connection;

    // 定义sql语句的执行对象
    private PreparedStatement pstmt;
    public EventForeUtils( ) {


    }

    public EventForeUtils(Connection connection) {
        this.connection = connection;

    }

    /**
     * 执行更新操作
     *
     * @param sql    sql语句
     * @param params 执行参数
     * @return 执行结果
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<?> params)
            throws SQLException {
        boolean flag = false;
        int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数
        pstmt = connection.prepareStatement(sql);
        int index = 1;
        // 填充sql语句中的占位符
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        result = pstmt.executeUpdate();
        flag = result > 0;
        return flag;
    }

    // 保存对象 表名与类名相同
    public boolean saveObject(Object obj) throws Exception {
        boolean flag = insertObject(obj);
        releasePstmt();
        return flag;
    }

    // 保存对象 （未关闭数据库 资源）   表名与类名相同
    private  boolean insertObject(Object obj) throws Exception {
        Class cla = obj.getClass();
        String tableName = "m_bs_warning_info";
        //System.out.println(tableName);
        Field[] fields = cla.getDeclaredFields();
        List<Object> valueObj =  new ArrayList<>();
        StringBuffer sqlAgo = new StringBuffer("insert into " + tableName
                + " (");
        StringBuffer sqlAfter = new StringBuffer("");
        for (int i = 0; i < fields.length ; i++) {
            String fieldName = fields[i].getName();
            String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
            String methodName = "get" + methodNameBegin
                    + fieldName.substring(1);
            @SuppressWarnings({ "unchecked", "rawtypes" })
            Method method = cla.getMethod(methodName);
            Object tempObj = method.invoke(obj);
            if ( tempObj!= null) {
                valueObj.add(tempObj);
                sqlAgo.append(fieldName).append(",");
                sqlAfter.append("?,");
            }
        }
        if(valueObj.size() == 0){
            return false;
        }else{
            //最后一位为,，去除
            if (sqlAgo.charAt(sqlAgo.length() - 1) == ',') {
                sqlAgo.deleteCharAt(sqlAgo.length() - 1);
            }
            if (sqlAfter.charAt(sqlAfter.length() - 1) == ',') {
                sqlAfter.deleteCharAt(sqlAfter.length() - 1);
            }
            sqlAgo.append(") values(").append(sqlAfter).append(")");

            pstmt = connection.prepareStatement(sqlAgo.toString());
            for(int i = 1; i <= valueObj.size(); i++){
                pstmt.setObject(i,valueObj.get(i-1));
            }
            pstmt.executeUpdate();
        }
        return true;
    }




    /**
     * 释放资源
     */
    public void releaseConn() {

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 释放资源
     */
    public void releasePstmt() {

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Exception in keyword releasePstmt!", e);
            }
        }

    }

    public static void main(String[] args) {

//        try {
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
    }
}
