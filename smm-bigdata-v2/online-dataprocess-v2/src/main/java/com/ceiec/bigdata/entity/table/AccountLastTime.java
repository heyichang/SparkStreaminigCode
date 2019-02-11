package com.ceiec.bigdata.entity.table;

import java.sql.Timestamp;

/**
 * Created by heyichang on 2018/2/5.
 */
public class AccountLastTime {
    private String account_id;
    private Timestamp latest_time;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public Timestamp getLatest_time() {
        return latest_time;
    }

    public void setLatest_time(Timestamp latest_time) {
        this.latest_time = latest_time;
    }


}
