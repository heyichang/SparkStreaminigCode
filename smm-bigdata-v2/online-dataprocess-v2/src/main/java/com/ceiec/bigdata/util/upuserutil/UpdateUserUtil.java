package com.ceiec.bigdata.util.upuserutil;

import com.ceiec.bigdata.util.jdbcutil.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by heyichang on 2018/1/24.
 */
public class UpdateUserUtil {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserUtil.class);

    public static void updateEsVirtualOverDue(int recordId) {
        String sql = "UPDATE `m_bs_account_overdue`  SET modified = TRUE ,crawer_get =TRUE  where record_id = ?" ;

        JdbcUtils jdbcUtils = new JdbcUtils();
        try {
            jdbcUtils.execute(sql, recordId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("update overdue and esvi error");
        }finally {
            jdbcUtils.releaseConn();
        }
    }


    public static void updateOverDue( int recordId) {
        String sql = "UPDATE `m_bs_account_overdue`  SET crawer_get =TRUE  where record_id = ?" ;

        JdbcUtils jdbcUtils = new JdbcUtils();
        try {
            jdbcUtils.execute(sql, recordId);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("update overdue error");
        }finally {
            jdbcUtils.releaseConn();
        }
    }
}
