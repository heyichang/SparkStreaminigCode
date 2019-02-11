package com.ceiec.bigdata.dao.common;

import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.jdbcutil.JdbcUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:heyichang
 * @description:主要处理账号异常的一些表
 * @date:Created in 10:52 2017-12-19
 */
public class AccountCheckDao {
    private static Logger log = LogManager.getLogger(AccountCheckDao.class.getName());
    private volatile static AccountCheckDao singleton;

    /**
     * 获取AccountCheckDao的实例
     * @return
     */
    public static AccountCheckDao getSingleton() {
        if (singleton == null) {
            synchronized (AccountCheckDao.class) {
                if (singleton == null) {
                    singleton = new AccountCheckDao();
                }
            }
        }
        return singleton;
    }

    /**
     * 将获取的值都拼接为list返回
     * @param rs
     * @return
     * @throws SQLException
     */
    public List<String> convertList(ResultSet rs) throws SQLException {
        List<String> list = new ArrayList<>();
        //获取meta信息，包含由表和字段信息
        ResultSetMetaData md = rs.getMetaData();
        //获取列的数量
        int columnCount = md.getColumnCount();

        /**
         * 每一行都进行一次处理
         */
        while (rs.next()) {
            //将所有的字段信息都拼接到一个字符串中
            StringBuffer msg = new StringBuffer();
            for (int i = 1; i <= columnCount; i++) {
                //拼接字段名
                msg.append(md.getColumnName(i));
                if (i<columnCount){
                    msg.append("=").append(rs.getObject(i)).append("^");
                }
                else {
                    msg.append("=").append(rs.getObject(i));
                }
            }
            list.add(msg.toString());
        }
        return list;
    }


    /**
     * 存储预警信息
     * @param userId
     * @param warningConfId
     * @param monitorEntityId
     * @param monitorEntityName
     * @param warningTypeId
     * @param alarmModeIds
     * @param infoId
     * @param author
     * @param summary
     * @param content
     * @param siteId
     * @param siteTypeId
     * @param createPostTime
     * @param abnormalTime
     * @param normalPlace
     * @param abnormalPlace
     */
    public void saveCheckData(String userId,int warningConfId,String monitorEntityId,String monitorEntityName,
                              int warningTypeId,String alarmModeIds,String infoId,String author,String summary,
                              String content,int siteId,int siteTypeId,String createPostTime,String abnormalTime,
                              String normalPlace,String abnormalPlace)  {
        //获取程序开始时间
        long startTime=System.currentTimeMillis();

        /**
         * 设置一些无需外部传进来的参数
         * id warningTime status
         */
        //获取id
        String id = InfoIdUtils.get32UUID();
        //获取warning_time
        String warningTime = TimeUtils.getTime();
        //获取status
        int status = 2;
        //获取monitorTypeId
        int monitorTypeId =3;

        //写入代码
        String sql = "INSERT INTO `m_bs_warning_info` " +
                "(id,user_id,warning_conf_id,monitor_type_id,monitor_entity_id," +
                "monitor_entity_name,warning_type_id,alarm_mode_ids,status,info_id," +
                "author,summary,content,site_id,site_type_id," +
                "create_post_time,warning_time,abnormal_time,normal_place,abnormal_place) " +
                "VALUES(?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?)";

        JdbcUtils jdbcUtils = new JdbcUtils();
        try {
            jdbcUtils.execute(sql,
                    id,userId,warningConfId,monitorTypeId,monitorEntityId,
                    monitorEntityName,warningTypeId,alarmModeIds,status,infoId ,
                    author,summary,content,siteId ,siteTypeId,
                    createPostTime,warningTime,abnormalTime,normalPlace,abnormalPlace);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("account and location warning insert into mysql error from twitter");
        }finally {
            jdbcUtils.releaseConn();
        }

        //获取程序结束时间
        long endTime=System.currentTimeMillis();

    }

    /**
     * 获取accountId转换为List
     * @param rs
     * @return
     * @throws SQLException
     */
    public List<String> resultSetToList(ResultSet rs) throws SQLException {
        if (rs == null) {
            return new ArrayList<String>();
        }
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            String accountId = rs.getString("account_id");
            list.add(accountId);
        }
        return list;
    }
}
