package com.ceiec.bigdata.entity.twitter;


import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;

/**
 * Created by heyichang on 2017/10/15.
 */
public class TwitterRoot {

    private String crawlid;

    private TwitterRoot retweeted_status;

    private TwitterRoot quoted_status;

    private boolean possibly_sensitive;

    private Entities1 entities;

    private Long id;

    private ExtendedEntities extended_entities;

    private String contributors;

    private Long in_reply_to_status_id;

    private User user;

    private boolean is_quote_status;

    private String lang;

    private String in_reply_to_status_id_str;

    private Place place;

    private String crawled_time;

    private String timestamp_ms;

    private int retweet_count;

    private boolean favorited;

    private String created_at;

    private String source;

    private String in_reply_to_user_id_str;

    private boolean retweeted;

    private String geo;

    private String in_reply_to_screen_name;

    private int favorite_count;

    private String coordinates;

    private String text;
    private String full_text;

    private int reply_count;

    private int quote_count;

    private boolean truncated;

    private String id_str;

    private Long in_reply_to_user_id;

    private String filter_level;

    private Integer first_leval_type_id_manual;
    private Integer streaming;
    private ExtendedTweet extended_tweet;

    public String getCrawled_time() {
        return crawled_time;
    }

    public void setCrawled_time(String crawled_time) {
        this.crawled_time = crawled_time;
    }

    public ExtendedTweet getExtended_tweet() {
        return extended_tweet;
    }

    public void setExtended_tweet(ExtendedTweet extended_tweet) {
        this.extended_tweet = extended_tweet;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public Integer getStreaming() {
        return streaming;
    }

    public void setStreaming(Integer streaming) {
        this.streaming = streaming;
    }

    public boolean isPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public String getTimestamp_ms() {
        return timestamp_ms;
    }

    public void setTimestamp_ms(String timestamp_ms) {
        this.timestamp_ms = timestamp_ms;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public int getQuote_count() {
        return quote_count;
    }

    public void setQuote_count(int quote_count) {
        this.quote_count = quote_count;
    }

    public Integer getFirst_leval_type_id_manual() {
        return first_leval_type_id_manual;
    }

    public void setFirst_leval_type_id_manual(Integer first_leval_type_id_manual) {
        this.first_leval_type_id_manual = first_leval_type_id_manual;
    }

    public TwitterRoot getQuoted_status() {
        return quoted_status;
    }

    public void setQuoted_status(TwitterRoot quoted_status) {
        this.quoted_status = quoted_status;
    }

    public String getCrawlid() {
        return crawlid;
    }

    public void setCrawlid(String crawlid) {
        this.crawlid = crawlid;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public boolean isIs_quote_status() {
        return is_quote_status;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setFilter_level(String filter_level) {
        this.filter_level = filter_level;
    }

    public String getFilter_level() {
        return this.filter_level;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getId_str() {
        return this.id_str;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean getTruncated() {
        return this.truncated;
    }

    public void setEntities(Entities1 entities) {
        this.entities = entities;
    }

    public Entities1 getEntities() {
        return this.entities;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public void setIn_reply_to_status_id(Long in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public Long getIn_reply_to_status_id() {
        return this.in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public String getIn_reply_to_status_id_str() {
        return this.in_reply_to_status_id_str;
    }

    public void setIn_reply_to_user_id(Long in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public Long getIn_reply_to_user_id() {
        return this.in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    }

    public String getIn_reply_to_user_id_str() {
        return this.in_reply_to_user_id_str;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getIn_reply_to_screen_name() {
        return this.in_reply_to_screen_name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getGeo() {
        return this.geo;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return this.coordinates;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public String getContributors() {
        return this.contributors;
    }

    public void setRetweeted_status(TwitterRoot retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public TwitterRoot getRetweeted_status() {
        return this.retweeted_status;
    }

    public void setIs_quote_status(boolean is_quote_status) {
        this.is_quote_status = is_quote_status;
    }

    public boolean getIs_quote_status() {
        return this.is_quote_status;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getRetweet_count() {
        return this.retweet_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getFavorite_count() {
        return this.favorite_count;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean getFavorited() {
        return this.favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public boolean getRetweeted() {
        return this.retweeted;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return this.lang;
    }


    public String GatherTime = TimeUtils.getTime();
    public String GatherData = TimeUtils.getDate();

    public TwitterRoot() {

    }

    public ExtendedEntities getExtended_entities() {
        return extended_entities;
    }

    public void setExtended_entities(ExtendedEntities extended_entities) {
        this.extended_entities = extended_entities;
    }

    public String getRowKey() {

        if (null != this.user) {
            StringBuffer sb = new StringBuffer();
            sb.append("https://twitter.com/").append(user.getScreen_name())
                    .append("/status/").append(getId_str());
            return InfoIdUtils.generate32MD5ID(sb.toString());
        }
        return InfoIdUtils.get32UUID();
    }

    @Override
    public String toString() {
        return "TwitterRoot{" +
                "crawlid='" + crawlid + '\'' +
                ", retweeted_status=" + retweeted_status +
                ", possibly_sensitive=" + possibly_sensitive +
                ", entities=" + entities +
                ", id=" + id +
                ", extended_entities=" + extended_entities +
                ", contributors='" + contributors + '\'' +
                ", in_reply_to_status_id=" + in_reply_to_status_id +
                ", user=" + user +
                ", is_quote_status=" + is_quote_status +
                ", lang='" + lang + '\'' +
                ", in_reply_to_status_id_str='" + in_reply_to_status_id_str + '\'' +
                ", place=" + place +
                ", timestamp_ms='" + timestamp_ms + '\'' +
                ", retweet_count=" + retweet_count +
                ", favorited=" + favorited +
                ", created_at='" + created_at + '\'' +
                ", source='" + source + '\'' +
                ", in_reply_to_user_id_str='" + in_reply_to_user_id_str + '\'' +
                ", retweeted=" + retweeted +
                ", geo='" + geo + '\'' +
                ", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
                ", favorite_count=" + favorite_count +
                ", coordinates='" + coordinates + '\'' +
                ", text='" + text + '\'' +
                ", reply_count=" + reply_count +
                ", quote_count=" + quote_count +
                ", truncated=" + truncated +
                ", id_str='" + id_str + '\'' +
                ", in_reply_to_user_id=" + in_reply_to_user_id +
                ", filter_level='" + filter_level + '\'' +
                ", first_leval_type_id_manual=" + first_leval_type_id_manual +
                ", streaming=" + streaming +
                ", GatherTime='" + GatherTime + '\'' +
                ", GatherData='" + GatherData + '\'' +
                '}';
    }

    public String getC3() {
        return "";
    }


//    public void addRow(Table examplesTable) throws Exception{
//        Put put = new Put(Bytes.toBytes(getRowKey()));
//        // 参数分别:列族、列、值
//        put.addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C1), Bytes.toBytes("1"))
//                .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C2), Bytes.toBytes(this.GatherTime))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C3), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C4), Bytes.toBytes(this.text))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C5), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C6), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C7), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C8), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C9), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C10), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C11), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C12), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C13), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C14), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C15), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C16), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C17), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C18), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C19), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C20), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C21), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C22), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C23), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C24), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C25), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C26), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C27), Bytes.toBytes("1"))
//        .addColumn(Bytes.toBytes(Constants.Examples_TableInfo.FAMILY), Bytes.toBytes(Constants.Examples_TableInfo.C28), Bytes.toBytes("1"));
//
//        try {
//            examplesTable.put(put);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
