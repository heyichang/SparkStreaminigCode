package com.ceiec.graph.utils.mysqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author heyichang
 * description:批量写入数据
 */
public class BulkUtil {
    static Connection con = C3p0Utils.getConnection();
    static PreparedStatement stmt = null;

    public static int executeInsert() throws SQLException {
        int i = 0;
        //设置批量处理的数量
        int batchSize = 5000;
        stmt = con.prepareStatement("insert into mysqltest (id,name) "
                + "values (?,?)");

        // 关闭事务自动提交 ,这一行必须加上
        con.setAutoCommit(false);
        for (int j = 0; j < 50005; j++){
            ++i;
            stmt.setInt(1, j);
            stmt.setString(2, "name");
            stmt.addBatch();
            if ( i % batchSize == 0 ) {
                stmt.executeBatch();
                con.commit();
            }
        }
        if ( i % batchSize != 0 ) {
            stmt.executeBatch();
            con.commit();
        }
        return i;
    }



    /**
     * 添加单条记录
     * @param statement
     * @param start
     * @param end
     * @param type
     * @throws SQLException
     */
    public static PreparedStatement batchOfbuild(PreparedStatement statement,String start,String end,String type) throws SQLException {
          statement.setString(1,start);
          statement.setString(2,type);
          statement.setString(3,end);

          statement.addBatch();

          return  statement;
    }


    public static void main(String[] args) throws SQLException {
        /**
         * 测试批量添加关系数据
         */
        PreparedStatement statement = con.prepareStatement("insert IGNORE into recordREl (start,type,end) "
                + "values (?,?,?)");

        // 关闭事务自动提交 ,这一行必须加上
        con.setAutoCommit(false);


        batchOfbuild(statement,"hyc","test","end");
        batchOfbuild(statement,"hyc2","test","end");
        batchOfbuild(statement,"hyc","test","end");

        try{
        statement.executeBatch();
        con.commit();
        }
        catch (Exception ex){
            System.out.println("数据重复");
            ex.printStackTrace();
        }
    }
}
