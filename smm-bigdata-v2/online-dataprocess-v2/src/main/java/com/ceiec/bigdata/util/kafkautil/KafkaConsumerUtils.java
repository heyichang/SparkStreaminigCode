package com.ceiec.bigdata.util.kafkautil;



import com.ceiec.bigdata.util.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerUtils {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerUtils.class);

    private static volatile KafkaConsumerUtils instance = null;
    private static Properties props;
    private static KafkaConsumer<String, String> consumer ;
    static {
        props = new Properties();
        props.put(Constants.BOOKERSTRAP_SERVERS, Constants.BOOKER_HOST);
        props.put("group.id", "graph_test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }
    private KafkaConsumerUtils(){

    }
    public static KafkaConsumerUtils getInstance() {
        if (instance == null) {
            synchronized (KafkaConsumerUtils.class) {
                if (instance == null) {
                    instance = new KafkaConsumerUtils();
                }
            }
        }
        return instance;
    }

    public static boolean getbroadController(){
        return false;
    }



    public static void main(String[] args) {
        try{
            consumer.subscribe(Arrays.asList("k1995"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
            }
        }catch (Exception e){
            System.err.println("kafka consumer error");
        }finally {
            if(consumer != null){
                consumer.close();
            }
        }




    }
}
