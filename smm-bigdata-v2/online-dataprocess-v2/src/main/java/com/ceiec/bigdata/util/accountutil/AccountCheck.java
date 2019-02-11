package com.ceiec.bigdata.util.accountutil;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.common.AccountCheckDao;
import com.ceiec.bigdata.entity.table.event.EventForeMapping;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;


/**
 * @author:heyichang
 * @description:账号发文异常检测
 * @date:Created in 11:15 2017-12-24
 */
public class AccountCheck {
    private static Logger log = LogManager.getLogger(AccountCheck.class.getName());
    private AccountCheckDao accountCheckDaoImpl;

    public static void main(String[] args) {
    }

    public AccountCheck() {
    }

    /**
     * * 账号发文异常检测
     * description：需要获取每条信息的，infoId，contentOftext，accountId，timeSlot，regionId，账户统计信息,事件预警配置表数据
     * 对每一条的数据accountId->获取userId->获取userId的warningType->如果warningtype存在
     * 则执行该监控类型的判断
     * 将异常的信息存储入预警信息表
     *
     */
    public void accountCheckForTimeAndPlace(String infoId,String monitorEntityId,String author,String content,String summary,int siteId,int siteTypeId,String createPostTime,String timeSlot,String regionId,List<String> list, List<String> forelist) throws SQLException {

        //获取程序开始时间
        long startTime = System.currentTimeMillis();

        this.accountCheckDaoImpl = AccountCheckDao.getSingleton();

        //获取事件预警表Entity数据
        List<EventForeMapping> foreOfEventForeMappings = new ArrayList<EventForeMapping>();
        //循环转换Entity
        for (String eventFJson : forelist) {
            EventForeMapping eventForeMapping = JSON.parseObject(eventFJson, EventForeMapping.class);
            foreOfEventForeMappings.add(eventForeMapping);
        }

        //循环遍历匹配
        for (String msg : list) {

            String[] msgArr = msg.split("\\^");

            //获取checkAccount
            String checkAccount = msgArr[1].split("=")[1];

            //获取checkUserid
            String checkUserid = msgArr[2].split("=")[1];

            //获取checkAbTime
            String checkAbTime = msgArr[3].split("=")[1];

            //获取checkPlace
            String checkPlace = null;

            if (msgArr[4].split("=")[1] != null && msgArr[4].split("=")[1].trim().equals("null")){

            }else {
                 checkPlace = msgArr[4].split("=")[1];
            }

            //获取账号昵称
            String monitorEntityName = msgArr[5].split("=")[1];


            if (checkAccount.equals(monitorEntityId)) {

                //获取userId的WarningType
                String warningTypeId = null;

                //获取userId的warningConfId
                int warningConfId = 0;

                //预警方式
                String alarmModeIds = null;

                //获取abnormalplace
                String abnormalPlace = null;

                Long foreStartTime = System.currentTimeMillis();

                /**
                 * 通过checkUserid循环遍历预警配置表，获取Userid的数据，并获取相应的warningType
                 * 并获取相应的foreWarningId
                 */
                for (EventForeMapping eventForeMappingOfCheck : foreOfEventForeMappings) {
                    //匹配是否有userid
                    if (eventForeMappingOfCheck.getUser_id().equals(checkUserid)) {
                        warningTypeId = eventForeMappingOfCheck.getWarning_type().toString();
                        warningConfId = eventForeMappingOfCheck.getWarning_conf_id();
                        alarmModeIds = eventForeMappingOfCheck.getAlarm_mode();
                    }
                }
                long foreEndTime = System.currentTimeMillis();

                /**
                 * 判断两个条件，是否含有时间异常检测，是否含有非常规时间
                 */
                String[] checkAbTimes = checkAbTime.split("_");
                //数组转set集合
                Set<String> staffsSet = new HashSet<>(Arrays.asList(checkAbTimes));

                if (warningTypeId != null && warningTypeId.contains("1") && staffsSet.contains(timeSlot)) {
                    System.out.println("时间异常"+warningConfId);
                    //数据库存储
                    accountCheckDaoImpl.saveCheckData(checkUserid,Integer.valueOf(warningConfId),monitorEntityId,monitorEntityName,Integer.valueOf("1"),
                            alarmModeIds,infoId,author,summary,content,
                            siteId,siteTypeId,createPostTime,checkAbTime,checkPlace,abnormalPlace);
                }
                /**
                 * 判断两个条件，是否含有位置异常检测，是否含有常规位置
                 */
                if (warningTypeId != null && warningTypeId.contains("2") && checkPlace !=null && regionId !=null && (!(checkPlace.contains(regionId)))) {
                    System.out.println("位置异常"+warningConfId);
                    //给位置赋值
                    abnormalPlace = regionId;
                    //数据库存储
                    accountCheckDaoImpl.saveCheckData(checkUserid,Integer.valueOf(warningConfId),monitorEntityId,monitorEntityName,Integer.valueOf("2"),
                            alarmModeIds,infoId,author,summary,content,
                            siteId,siteTypeId,createPostTime,checkAbTime,checkPlace,abnormalPlace);
                }
            }
        }

        //获取程序结束时间
        long endTime = System.currentTimeMillis();
    }

}
