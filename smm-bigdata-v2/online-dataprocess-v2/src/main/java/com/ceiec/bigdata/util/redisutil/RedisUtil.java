package com.ceiec.bigdata.util.redisutil;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Created by heyichang on 2018/1/17.
 */
public class RedisUtil {

    //Redis服务器IP
    private static String ADDR = "172.16.3.72";
    //Redis的端口号
    private static int PORT = 6379;
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭jedis资源
     * @param jedis
     */
    public static void close(final Jedis jedis) {
        if (jedis != null){
            jedis.close();
        }

    }


    /**
     * 批量导入redis
     * @param jedis
     * @param key
     * @param list
     */
    public static void sAdd(Jedis jedis ,String key,List<String> list) {
        if (jedis != null && list.size()>0){
            String[] strings = new String[list.size()];
            jedis.sadd(key,list.toArray(strings));
        }

    }

    /**
     * 查询成员是否存在
     * @param jedis
     * @param key
     * @param member
     */
    public static Boolean isIdExists(Jedis jedis ,String key,String member) {
        if (jedis != null){
            //jedis.configSet("timeout", "30");
            boolean flag = jedis.sismember(key, member);
            return flag;
        }
        return null;

    }

    public static void main(String[] args) {

        Jedis jedis = RedisUtil.getJedis();
        for ( int i = 0 ;i <5 ;i++){
            long s = System.currentTimeMillis();
            boolean flag = RedisUtil.isIdExists(jedis,"es_info_id","03D4586A0400219385193DFD7D8D7367");
            System.out.println(flag);

            long e = System.currentTimeMillis();
            System.out.println(e-s);
        }

        RedisUtil.close(jedis);
    }


}
