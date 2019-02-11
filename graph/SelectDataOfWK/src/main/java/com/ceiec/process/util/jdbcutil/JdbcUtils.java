package com.ceiec.process.util.jdbcutil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by zoumengcheng on 2017/10/17.
 */
public class JdbcUtils {
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;


    static org.apache.log4j.Logger logger=org.apache.log4j.Logger.getLogger(JdbcUtils.class.getName());


    public  Connection getConnection() {

        return C3p0Utils.getConnection();
    }


    public int execute(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        PreparedStatement stmt = null;
        int result = -1;
        try {
            stmt = createPreparedStatement(conn, sql, params);
            result = stmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            C3p0Utils.closeConn(conn);
            C3p0Utils.closePst(stmt);
        }
        return result;
    }

    public <T> T findSimpleRefResult(String sql, List<Object> params,
                                     Class<T> cls )throws Exception{
        Connection connection= C3p0Utils.getConnection();
        T resultObject = null;
        int index = 1;
        try {
        pstmt = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()){
            for(int i = 0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData  = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while(resultSet.next()){
            //通过反射机制创建一个实例
            resultObject = cls.newInstance();
            for(int i = 0; i<cols_len; i++){
                String cols_name = metaData.getColumnName(i+1);
                Object cols_value = resultSet.getObject(cols_name);
                if(cols_value == null){
                    cols_value = "";
                }
                Field field = cls.getDeclaredField(cols_name);
                field.setAccessible(true); //打开javabean的访问权限
                field.set(resultObject, cols_value);
            }
            return resultObject;
        }} catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3p0Utils.closeConn(connection);
            C3p0Utils.closePst(pstmt);
        }
        return null;

    }

    /**通过反射机制查询多条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> List<T> findMoreRefResult(String sql, List<Object> params,
                                         Class<T> cls )throws Exception {
        Connection connection= C3p0Utils.getConnection();
        List<T> list = new ArrayList<T>();
        int index = 1;
        try {
        pstmt = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()){
            for(int i = 0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData  = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while(resultSet.next()){
            //通过反射机制创建一个实例
            T resultObject = cls.newInstance();
            for(int i = 0; i<cols_len; i++){
                String cols_name = metaData.getColumnName(i+1);
                Object cols_value = resultSet.getObject(cols_name);
                if(cols_value == null){
                    cols_value = "";
                }
                Field field = cls.getDeclaredField(cols_name);
                field.setAccessible(true); //打开javabean的访问权限
                field.set(resultObject, cols_value);
            }
            list.add(resultObject);
        }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3p0Utils.closeConn(connection);
            C3p0Utils.closePst(pstmt);
        }
        return null;
    }

    /**
     * 执行查询操作
     *
     * @param sql    sql语句
     * @param params 执行参数
     * @return
     * @throws SQLException
     */
    public List<Set<String>> findResultSet(String sql, List<?> params)
            throws SQLException {
        List<Set<String>> list = new ArrayList<Set<String>>();
        int index = 1;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(index++, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols_len = metaData.getColumnCount();
        while (resultSet.next()) {
            Set<String> set = new HashSet<String>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = metaData.getColumnName(2);
                Object cols_value = resultSet.getObject(cols_name);
                if (cols_value == null) {
                    cols_value = "";
                }
                set.add((String) cols_value);
            }
            list.add(set);
        }
        return list;
    }



    public PreparedStatement createPreparedStatement(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++)
            stmt.setObject(i + 1, params[i]);
        return stmt;
    }

    /**
     * 释放资源
     */
    public void releaseConn() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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


    public  void close(Connection conn, PreparedStatement pst, ResultSet rs){
        C3p0Utils.closeAll(conn,pst,rs);
    }
    public  void closeConn(Connection conn){
        C3p0Utils.closeConn(conn);
    }
    public  void closePst(PreparedStatement pst){
        C3p0Utils.closePst(pst);
    }
    public  void closeRes( ResultSet rs){
        C3p0Utils.closeRes(rs);
    }
}
