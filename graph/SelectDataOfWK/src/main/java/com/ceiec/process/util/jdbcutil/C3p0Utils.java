package com.ceiec.process.util.jdbcutil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zoumengcheng on 2017/10/16.
 */
public class C3p0Utils {
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(C3p0Utils.class.getName());

    //通过标识名来创建相应连接池
    static ComboPooledDataSource dataSource = new ComboPooledDataSource("mysql");

    //从连接池中取用一个连接
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();

        } catch (Exception e) {
            logger.error("Exception in C3p0Utils!", e);
            throw new Error("获取数据库连接出错!", e);
        }
    }

    //释放连接回连接池
    public static void closeAll(Connection conn, PreparedStatement pst, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库连接关闭出错!", e);
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库连接关闭出错!", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库连接关闭出错!", e);
            }
        }
    }

    //释放连接回连接池
    public static void closeConn(Connection conn) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库连接关闭出错!", e);
            }
        }
    }

    public static void closePst(PreparedStatement pst) {

        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库预声明连接关闭出错!", e);
            }
        }


    }

    public static void closeRes(ResultSet res) {

        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) {
                logger.error("Exception in C3p0Utils!", e);
                throw new Error("数据库结果集连接关闭出错!", e);
            }
        }
    }

}
