package com.ceiec.graph.util;

/**
 * Created by zoumengcheng on 2017/8/26.
 */
public class Constants {
    public static final String STREAMING_TEST = "streaming_hbase";
//10.11.13.4  172.16.3.31,172.16.3.34,172.16.3.36 172.16.3.31:6667,172.16.3.32:6667,172.16.3.33:6667
    public static final String EXAMPLES_TEST = "t1";
    public static final String FAMILY = "cf";
    public static final String KAFKABROKERLIST = "10.11.13.4 :6667";
    public static final String KAFKABROKERLIST_TEST = "192.168.19.71:9092";
    public static final String CONSUMER_GROUP = "test_streaming123";
    public static final String HBASE_ZOOKEEPER_CLIENTPORT = "hbase.zookeeper.property.clientPort";
    public static final String ZOOKEEPER_PORT = "2181";
    public static final String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    public static final String ZOOKEEPER_HOST = "10.11.13.4";
    public static final String ZK_LIST = "10.11.13.4:2181";
    public static final String ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent";
    public static final String PARENT_DIR = "/hbase-unsecure";
    public static final String ZOOKEEPER_SESSION_TIMEOUT = "zookeeper.session.timeout";
    public static final int DEFAULT_ZK_SESSION_TIMEOUT = 180000;


    public static final String INFO_INDEX = "m_es_info";//m_es_info
    public static final String INFO_TYPE = "info";

    public static final String VIRTUAL_INDEX = "m_es_virtual_account_base_info";//m_es_virtual_account_base_info
    public static final String ACCOUNT = "account";

    public static final String INFO_INDEX2 = "m_es_info_dev";//m_es_info  m_es_info_dev  news_test
    public static final String INFO_TYPE2 = "info"; //info_test

    public static final String VIRTUAL_INDEX2 = "m_es_virtual_account_base_info_dev";//m_es_virtual_account_base_info_dev  es_virtual_account_base_info_2
    public static final String ACCOUNT2 = "account";//account_test


    public static final class examplesTableInfo {

        public static final String NEWS_TABLE_NAME = "crawled";
        public static final String NEWS_TABLE_NAME2 = "crawled_dev";
        public static final String FAMILY = "result";
        public static final String CREATING_TIME = "creating_time";
        public static final String GATHER_TIME = "gather_time";
        public static final String SENSITIVE_ID = "sensitive_id";
        public static final String CONTENT = "content";
        public static final String SUMMARY = "summary";
        public static final String TITLE = "title";
        public static final String SOURCE_CONTENT = "source_content";
        public static final String SOURCE_URL = "source_url";
        public static final String HOTWORD_JSON = "hot_word_json";
        public static final String ENTITY_JSON = "entity_json";
        public static final String ENTITY_OF_TWITTER_JSON = "entity_of_twitter_json";
        public static final String SITE_ID = "site_id";
        public static final String SITE_TYPE_ID = "site_type_id";
        public static final String REMARK_NUMBER = "remark_number";
        public static final String RETWEET_NUMBER = "retweet_number";
        public static final String LIKE_NUMBER = "like_number";
        public static final String ACCOUNT_ID = "account_id";
        public static final String ACCOUNT_NICK_NAME = "account_nick_name";
        public static final String FIRST_LEVAL_TYPE_ID = "first_leval_type_id";
        public static final String FIRST_LEVAL_TYPE_ID_MANUAL = "first_leval_type_id_manual";
        public static final String SECOND_LEVAL_TYPE_ID = "second_leval_type_id";
        public static final String IS_RETWEET = "is_retweet";
        public static final String RETWEET_INFO_ID = "retweet_info_id";
        public static final String TIME_SLOT = "time_slot";
        public static final String LONGITUDE = "Longitude";
        public static final String LATITUDE = "Latitude";
        public static final String REGION_ID = "region_id";
        public static final String LANGUAGE = "language";
        public static final String EMOTION_ID = "emotion_id";
        public static final String CREATING_DATE = "creating_date";
        public static final String GATHER_DATE = "gather_date";
        public static final String LOCATION_TYPE = "location_type";
        public static final String REGION_ID_STR = "region_id_str";
        public static final String PARENT_REGION_ID_STR = "parent_region_id_str";
        public static final String VERIFIED = "verified";
        public static final String DEVICE = "device";
        public static final String FROM_STREAMING = "from_streaming";
        public static final String TWITTER_STATUS_ID = "twitter_status_id";

    }

    public static final class esVirtualTableInfo {

        public static final String ACCOUNT_ID = "account_id";
        public static final String PORTRAIT = "portrait";
        public static final String ACCOUNT_NAME = "account_name";
        public static final String SITE_ID = "site_id";
        public static final String NICK_NAME = "nick_name";
        public static final String REGIST_REGION_ID = "regist_region_id";
        public static final String HOME = "home";
        public static final String POST_NUMBER = "post_number";
        public static final String FANS_NUMBER = "fans_number";
        public static final String FOLLOW_NUMBER = "follow_number";
        public static final String TWITTED_NUMBER = "twitted_number";
        public static final String NEGATIVE_NUMBER = "negative_number";
        public static final String SENSITIVE_NUMBER = "sensitive_number";
        public static final String IS_MONITOR = "is_monitor";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String FOLLOW_LIST = "follow_list";
        public static final String FRIENDS_LIST = "friends_list";
        public static final String FRIENDS_NUM = "friends_num";
        public static final String INTRODUCE = "introduce";
        public static final String BIRTHDAY = "birthday";
        public static final String GENDAR = "gendar";
        public static final String HOMETOWN = "hometown";
        public static final String TIMEZONE = "timezone";
        public static final String LANGUAGE = "language";
        public static final String EDUCATION = "education";
        public static final String WORK = "work";
        public static final String TIME = "time";
        public static final String FANS = "fans";

    }



    public static final class statsHbaseTable {

        public static final String TABLE_NAME = "crawled_stats";
        public static final String CF = "by_hour";

    }


    public static final class esTableInfo {
        //        public static final String INDEX = "info";
//        public static final String TYPE = "cf";

        public static final String C1 = "info_id";
        public static final String LOCATION = "location";
        public static final String LOCATION_LAT = "lat";
        public static final String LOCATION_LON = "lon";



        public static final String INDEX = "hello-es";
        public static final String TYPE = "hi-es";
    }

    public static final class messageType {
        //        public static final String INDEX = "info";
//        public static final String TYPE = "cf";
        public static final String NEWS = "diplomat_news";
        public static final String TWITTER = "twitter";
        public static final String TWITTER2 = "example";
        public static final String FORUM = "diplomat_forum";
        public static final String BLOG = "diplomat_blog";

    }

    public static final class httpConstants{
        public static final String URL = "http://192.168.19.126:8188/text/processData";
        public static final String SUMMARY = "summary";
        public static final String SENTIMENT = "sentiment";
        public static final String HOT_WORD = "hotword";
        public static final String CATGORY = "catgory";
        public static final String ENTITIES = "entities";
        public static final String TYPE = "type";
        public static final String CONTENT = "content";
    }


    public static final class nlpConstants{
        public static final String SENSITIVE_ID = "SensitiveId";
        public static final String SUMMARY = "Summary";
        public static final String TITLE = "Title";
        public static final String HOT_WORDS = "HotWords";

        public static final String ENTITIES = "Entities";
        public static final String ENYITY = "Entity";
        public static final String FIRST_LEVAL_TYPE_ID = "FirstLevalTypeId";
        public static final String SECOND_LEVAL_TYPE_ID = "SecondLevalTypeId";
        public static final String EMOTION_ID = "EmotionId";


        public static final String WORD = "word";
        public static final String SCORE = "score";
        public static final String INDICES = "indices";
        public static final String TYPE = "type";


        public static final String PHOTO = "photo";
        public static final String VIDEO = "video";
        public static final String THUMB = "thumb";
        public static final String LARGE = "large";
        public static final String MEDIUM = "medium";
        public static final String SMALL = "small";

        public static final String TEXT = "text";
        public static final String HASHTAGS = "hashtags";
        public static final String DISPLAY_URL = "display_url";
        public static final String EXPANDED_URL = "expanded_url";
        public static final String URL = "url";
        public static final String URLS = "urls";
        public static final String NAME = "name";
        public static final String ID_STR = "id_str";
        public static final String SCREEN_NAME = "screen_name";
        public static final String ID = "id";
        public static final String USER_MENTIONS = "user_mentions";
        public static final String EN = "en";

    }

}
