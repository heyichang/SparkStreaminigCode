package com.ceiec.bigdata.util.kafkautil;

import com.ceiec.bigdata.util.Constants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaUtil {
    private String topic;

    private Producer<String, String> producer;

    public KafkaUtil(String topic){
        this.topic=topic;
        this.producer=new KafkaProducer<String, String>(getConfig());
    }

    public static Properties getConfig() {
        Properties props =new Properties();
        props.put(Constants.BOOKERSTRAP_SERVERS, Constants.BOOKER_HOST);
        props.put(Constants.KEY_SERIALIZER, Constants.KEY_SERIALIZER_VALUE);
        props.put(Constants.VALUE_SERIALIZER, Constants.VALUE_SERIALIZER_VALUE);
        return props;
    }

    public void sendMessage(String key,String message){
        try {
            producer.send(new ProducerRecord<String, String>(topic,key,message));
        }
        catch (Exception e){
            System.out.println("kafka insert exception");
        }finally {
            producer.close();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        KafkaUtil myTestProducer = new KafkaUtil("k1995");
//        WarningOutput warningOutput = new WarningOutput();
//        warningOutput.setId(InfoIdUtils.get32UUID());
//        warningOutput.setWarning_conf_id(1);
//        warningOutput.setWarning_module_id(1);
//        warningOutput.setWarning_entity_id("1");
//        warningOutput.setWarning_entity_name("trump");
//        warningOutput.setWarning_level(1);
//        warningOutput.setInfo_id("965DC00586AD847C90C27B56E86D1DCA");
//        warningOutput.setUser_id("123456");
//        warningOutput.setMatch_words("trump");
//        warningOutput.setWarning_type_id(1);
//        warningOutput.setAuthor("Ricky Davila");
//        warningOutput.setSite_id(301);
//        warningOutput.setStatus(1);
//        //warningOutput.setSensitive_id(warningInfoMapping);
//        //warningOutput.setSummary();推特无摘要
//        warningOutput.setWarning_content("After he failed to complete his military service in Germany, donald trump’s grandfather, Friedrich Trump wrote a letter begging not to be deported. Here it is:\n" +
//                "https://t.co/S3xIA7i07x");
//        warningOutput.setWarning_time(TimeUtils.getTime());
//        warningOutput.setCreate_post_time("2018-05-28 23:57:18");
//        warningOutput.setImage_urls("https://twitter.com/TheRickyDavila/status/1001251096277417984");
//        String str = JSON.toJSONString(warningOutput);
        for (int i = 0; i < 1; i++) {
//            myTestProducer.sendMessage(null, str);
            myTestProducer.sendMessage(null,"{\"created_at\": \"Tue Apr 24 01:06:08 +0000 2018\", \"id\": 988584844123869184, \"id_str\": \"988584844123869184\", \"full_text\": \"Les compartimos esta imagen que habla por si sola\\n \\\"El chiste se cuenta solo...\\nLlegan a este palacio diciendo que si no vota por Duque nos convertiremos en Venezuela\\\" son tan c\\u00ednicos los Uribistas.\\n En definitiva @petrogustavo\\n#PetroEsLaEsperanza #PetroPresidente https://t.co/Sk5BIrPMLy\", \"truncated\": false, \"display_text_range\": [0, 264], \"entities\": {\"hashtags\": [{\"text\": \"PetroEsLaEsperanza\", \"indices\": [228, 247]}, {\"text\": \"PetroPresidente\", \"indices\": [248, 264]}], \"symbols\": [], \"user_mentions\": [{\"screen_name\": \"petrogustavo\", \"name\": \"Gustavo Petro\", \"id\": 49849732, \"id_str\": \"49849732\", \"indices\": [214, 227]}], \"urls\": [], \"media\": [{\"id\": 988584822837833733, \"id_str\": \"988584822837833733\", \"indices\": [265, 288], \"media_url\": \"http://pbs.twimg.com/media/DbgoqJuXcAUeDjA.jpg\", \"media_url_https\": \"https://pbs.twimg.com/media/DbgoqJuXcAUeDjA.jpg\", \"url\": \"https://t.co/Sk5BIrPMLy\", \"display_url\": \"pic.twitter.com/Sk5BIrPMLy\", \"expanded_url\": \"https://twitter.com/JvenesProgresis/status/988584844123869184/photo/1\", \"type\": \"photo\", \"sizes\": {\"thumb\": {\"w\": 150, \"h\": 150, \"resize\": \"crop\"}, \"small\": {\"w\": 443, \"h\": 680, \"resize\": \"fit\"}, \"medium\": {\"w\": 720, \"h\": 1106, \"resize\": \"fit\"}, \"large\": {\"w\": 720, \"h\": 1106, \"resize\": \"fit\"}}}]}, \"extended_entities\": {\"media\": [{\"id\": 988584822837833733, \"id_str\": \"988584822837833733\", \"indices\": [265, 288], \"media_url\": \"http://pbs.twimg.com/media/DbgoqJuXcAUeDjA.jpg\", \"media_url_https\": \"https://pbs.twimg.com/media/DbgoqJuXcAUeDjA.jpg\", \"url\": \"https://t.co/Sk5BIrPMLy\", \"display_url\": \"pic.twitter.com/Sk5BIrPMLy\", \"expanded_url\": \"https://twitter.com/JvenesProgresis/status/988584844123869184/photo/1\", \"type\": \"photo\", \"sizes\": {\"thumb\": {\"w\": 150, \"h\": 150, \"resize\": \"crop\"}, \"small\": {\"w\": 443, \"h\": 680, \"resize\": \"fit\"}, \"medium\": {\"w\": 720, \"h\": 1106, \"resize\": \"fit\"}, \"large\": {\"w\": 720, \"h\": 1106, \"resize\": \"fit\"}}}]}, \"source\": \"<a href=\\\"http://twitter.com/download/android\\\" rel=\\\"nofollow\\\">Twitter for Android</a>\", \"in_reply_to_status_id\": null, \"in_reply_to_status_id_str\": null, \"in_reply_to_user_id\": null, \"in_reply_to_user_id_str\": null, \"in_reply_to_screen_name\": null, \"user\": {\"id\": 304793712, \"id_str\": \"304793712\", \"name\": \"Juventudes Progresistas\", \"screen_name\": \"JvenesProgresis\", \"location\": \"Colombia\", \"description\": \"Juventudes Progresistas\", \"url\": null, \"entities\": {\"description\": {\"urls\": []}}, \"protected\": false, \"followers_count\": 13866, \"friends_count\": 1367, \"listed_count\": 45, \"created_at\": \"Wed May 25 03:37:41 +0000 2011\", \"favourites_count\": 116, \"utc_offset\": null, \"time_zone\": null, \"geo_enabled\": true, \"verified\": false, \"statuses_count\": 32123, \"lang\": \"es\", \"contributors_enabled\": false, \"is_translator\": false, \"is_translation_enabled\": false, \"profile_background_color\": \"FFFFFF\", \"profile_background_image_url\": \"http://abs.twimg.com/images/themes/theme1/bg.png\", \"profile_background_image_url_https\": \"https://abs.twimg.com/images/themes/theme1/bg.png\", \"profile_background_tile\": false, \"profile_image_url\": \"http://pbs.twimg.com/profile_images/900976009708699648/FSzJis1M_normal.jpg\", \"profile_image_url_https\": \"https://pbs.twimg.com/profile_images/900976009708699648/FSzJis1M_normal.jpg\", \"profile_link_color\": \"8AC72E\", \"profile_sidebar_border_color\": \"FFD500\", \"profile_sidebar_fill_color\": \"FFFFFF\", \"profile_text_color\": \"1994E6\", \"profile_use_background_image\": true, \"has_extended_profile\": false, \"default_profile\": false, \"default_profile_image\": false, \"following\": false, \"follow_request_sent\": false, \"notifications\": false, \"translator_type\": \"none\"}, \"geo\": null, \"coordinates\": null, \"place\": null, \"contributors\": null, \"is_quote_status\": false, \"retweet_count\": 21, \"favorite_count\": 35, \"favorited\": false, \"retweeted\": false, \"possibly_sensitive\": false, \"possibly_sensitive_appealable\": false, \"lang\": \"es\"}");
        }
    }
}
