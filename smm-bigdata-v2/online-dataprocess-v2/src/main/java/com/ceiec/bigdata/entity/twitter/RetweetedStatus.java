package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichange on 2017/11/10.
 */
public class RetweetedStatus {


    private User2 user;
    private String text;
    private Place place;
    private String id_str;
    private String filter_level;
    private String created_at;
    private Long id;
    private boolean is_quote_status;
    private String in_reply_to_screen_name;
    private String source;
    private String contributors;
    private int reply_count;
    private Long in_reply_to_status_id;
    private int quote_count;
    private boolean favorited;
    private int retweet_count;
    private List<Integer> display_text_range;
    private String in_reply_to_user_id_str;
    private int favorite_count;
    private boolean truncated;
    private Entities1 entities;
    private ExtendedEntities extended_entities;
    private Long in_reply_to_user_id;
    private String coordinates;
    private String in_reply_to_status_id_str;
    private boolean possibly_sensitive;
    private String lang;
    private boolean retweeted;
    private String geo;

    public String getFilter_level() {
        return filter_level;
    }

    public void setFilter_level(String filter_level) {
        this.filter_level = filter_level;
    }

    public boolean isIs_quote_status() {
        return is_quote_status;
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

    public boolean isFavorited() {
        return favorited;
    }

    public List<Integer> getDisplay_text_range() {
        return display_text_range;
    }

    public void setDisplay_text_range(List<Integer> display_text_range) {
        this.display_text_range = display_text_range;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public ExtendedEntities getExtended_entities() {
        return extended_entities;
    }

    public void setExtended_entities(ExtendedEntities extended_entities) {
        this.extended_entities = extended_entities;
    }

    public boolean isPossibly_sensitive() {
        return possibly_sensitive;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setId_str(String id_str){
        this.id_str = id_str;
    }
    public String getId_str(){
        return this.id_str;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }
    public void setTruncated(boolean truncated){
        this.truncated = truncated;
    }
    public boolean getTruncated(){
        return this.truncated;
    }
    public void setEntities(Entities1 entities){
        this.entities = entities;
    }
    public Entities1 getEntities(){
        return this.entities;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }

    public void setIn_reply_to_status_id(Long in_reply_to_status_id){
        this.in_reply_to_status_id = in_reply_to_status_id;
    }
    public Long getIn_reply_to_status_id(){
        return this.in_reply_to_status_id;
    }
    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str){
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }
    public String getIn_reply_to_status_id_str(){
        return this.in_reply_to_status_id_str;
    }
    public void setIn_reply_to_user_id(Long in_reply_to_user_id){
        this.in_reply_to_user_id = in_reply_to_user_id;
    }
    public Long getIn_reply_to_user_id(){
        return this.in_reply_to_user_id;
    }
    public void setIn_reply_to_user_id_str(String in_reply_to_user_id_str){
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    }
    public String getIn_reply_to_user_id_str(){
        return this.in_reply_to_user_id_str;
    }
    public void setIn_reply_to_screen_name(String in_reply_to_screen_name){
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }
    public String getIn_reply_to_screen_name(){
        return this.in_reply_to_screen_name;
    }
    public void setUser(User2 user){
        this.user = user;
    }
    public User2 getUser(){
        return this.user;
    }
    public void setGeo(String geo){
        this.geo = geo;
    }
    public String getGeo(){
        return this.geo;
    }
    public void setCoordinates(String coordinates){
        this.coordinates = coordinates;
    }
    public String getCoordinates(){
        return this.coordinates;
    }
    public void setPlace(Place place){
        this.place = place;
    }
    public Place getPlace(){
        return this.place;
    }
    public void setContributors(String contributors){
        this.contributors = contributors;
    }
    public String getContributors(){
        return this.contributors;
    }
    public void setIs_quote_status(boolean is_quote_status){
        this.is_quote_status = is_quote_status;
    }
    public boolean getIs_quote_status(){
        return this.is_quote_status;
    }
    public void setRetweet_count(int retweet_count){
        this.retweet_count = retweet_count;
    }
    public int getRetweet_count(){
        return this.retweet_count;
    }
    public void setFavorite_count(int favorite_count){
        this.favorite_count = favorite_count;
    }
    public int getFavorite_count(){
        return this.favorite_count;
    }
    public void setFavorited(boolean favorited){
        this.favorited = favorited;
    }
    public boolean getFavorited(){
        return this.favorited;
    }
    public void setRetweeted(boolean retweeted){
        this.retweeted = retweeted;
    }
    public boolean getRetweeted(){
        return this.retweeted;
    }
    public void setPossibly_sensitive(boolean possibly_sensitive){
        this.possibly_sensitive = possibly_sensitive;
    }
    public boolean getPossibly_sensitive(){
        return this.possibly_sensitive;
    }
    public void setLang(String lang){
        this.lang = lang;
    }
    public String getLang(){
        return this.lang;
    }

    @Override
    public String toString() {
        return "{" +
                "user=" + user +
                ", text='" + text + '\'' +
                ", place='" + place + '\'' +
                ", id_str='" + id_str + '\'' +
                ", filter_level='" + filter_level + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id=" + id +
                ", is_quote_status=" + is_quote_status +
                ", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
                ", source='" + source + '\'' +
                ", contributors='" + contributors + '\'' +
                ", reply_count=" + reply_count +
                ", in_reply_to_status_id=" + in_reply_to_status_id +
                ", quote_count=" + quote_count +
                ", favorited=" + favorited +
                ", retweet_count=" + retweet_count +
                ", display_text_range=" + display_text_range +
                ", in_reply_to_user_id_str='" + in_reply_to_user_id_str + '\'' +
                ", favorite_count=" + favorite_count +
                ", truncated=" + truncated +
                ", entities=" + entities +
                ", extended_entities=" + extended_entities +
                ", in_reply_to_user_id=" + in_reply_to_user_id +
                ", coordinates='" + coordinates + '\'' +
                ", in_reply_to_status_id_str='" + in_reply_to_status_id_str + '\'' +
                ", possibly_sensitive=" + possibly_sensitive +
                ", lang='" + lang + '\'' +
                ", retweeted=" + retweeted +
                ", geo='" + geo + '\'' +
                '}';
    }
}
