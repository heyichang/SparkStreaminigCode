package com.ceiec.bigdata.entity.twitter;

/**
 * Created by heyichang on 2017/11/10.
 */
public class User {

    private Long id;

    private Entities2 entities;

    private boolean protecteds;


    private String profile_banner_url;


    private boolean default_profile;

    private boolean default_profile_image;

    private boolean following;

    private boolean follow_request_sent;

    private boolean notifications;

    private String translator_type;

    private Place place;

    private String profile_background_color;


    private String profile_sidebar_fill_color;

    private String profile_image_url;

    private int friends_count;

    private String url;

    private boolean profile_use_background_image;

    private int listed_count;

    private String profile_link_color;

    private String lang;

    private boolean profile_background_tile;

    private String location;

    private String description;

    private boolean has_extended_profile;


    private String profile_image_url_https;

    private int favourites_count;

    private String id_str;

    private String profile_sidebar_border_color;

    private String profile_background_image_url;

    private int followers_count;


    private boolean geo_enabled;

    private int statuses_count;

    private boolean verified;

    private String profile_background_image_url_https;

    private String name;

    private String created_at;


    private String time_zone;

    private boolean is_translator;


    private String utc_offset;

    private boolean is_translation_enabled;

    private boolean contributors_enabled;


    private String screen_name;

    private String profile_text_color;
    private String errors;


    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
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
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setScreen_name(String screen_name){
        this.screen_name = screen_name;
    }
    public String getScreen_name(){
        return this.screen_name;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return this.location;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setEntities(Entities2 entities){
        this.entities = entities;
    }
    public Entities2 getEntities(){
        return this.entities;
    }
    public void setProtected(boolean protecteds){
        this.protecteds = protecteds;
    }
    public boolean getProtected(){
        return this.protecteds;
    }
    public void setFollowers_count(int followers_count){
        this.followers_count = followers_count;
    }
    public int getFollowers_count(){
        return this.followers_count;
    }
    public void setFriends_count(int friends_count){
        this.friends_count = friends_count;
    }
    public int getFriends_count(){
        return this.friends_count;
    }
    public void setListed_count(int listed_count){
        this.listed_count = listed_count;
    }
    public int getListed_count(){
        return this.listed_count;
    }
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
    public String getCreated_at(){
        return this.created_at;
    }
    public void setFavourites_count(int favourites_count){
        this.favourites_count = favourites_count;
    }
    public int getFavourites_count(){
        return this.favourites_count;
    }
    public void setUtc_offset(String utc_offset){
        this.utc_offset = utc_offset;
    }
    public String getUtc_offset(){
        return this.utc_offset;
    }
    public void setTime_zone(String time_zone){
        this.time_zone = time_zone;
    }
    public String getTime_zone(){
        return this.time_zone;
    }
    public void setGeo_enabled(boolean geo_enabled){
        this.geo_enabled = geo_enabled;
    }
    public boolean getGeo_enabled(){
        return this.geo_enabled;
    }
    public void setVerified(boolean verified){
        this.verified = verified;
    }
    public boolean getVerified(){
        return this.verified;
    }
    public void setStatuses_count(int statuses_count){
        this.statuses_count = statuses_count;
    }
    public int getStatuses_count(){
        return this.statuses_count;
    }
    public void setLang(String lang){
        this.lang = lang;
    }
    public String getLang(){
        return this.lang;
    }
    public void setContributors_enabled(boolean contributors_enabled){
        this.contributors_enabled = contributors_enabled;
    }
    public boolean getContributors_enabled(){
        return this.contributors_enabled;
    }
    public void setIs_translator(boolean is_translator){
        this.is_translator = is_translator;
    }
    public boolean getIs_translator(){
        return this.is_translator;
    }
    public void setIs_translation_enabled(boolean is_translation_enabled){
        this.is_translation_enabled = is_translation_enabled;
    }
    public boolean getIs_translation_enabled(){
        return this.is_translation_enabled;
    }
    public void setProfile_background_color(String profile_background_color){
        this.profile_background_color = profile_background_color;
    }
    public String getProfile_background_color(){
        return this.profile_background_color;
    }
    public void setProfile_background_image_url(String profile_background_image_url){
        this.profile_background_image_url = profile_background_image_url;
    }
    public String getProfile_background_image_url(){
        return this.profile_background_image_url;
    }
    public void setProfile_background_image_url_https(String profile_background_image_url_https){
        this.profile_background_image_url_https = profile_background_image_url_https;
    }
    public String getProfile_background_image_url_https(){
        return this.profile_background_image_url_https;
    }
    public void setProfile_background_tile(boolean profile_background_tile){
        this.profile_background_tile = profile_background_tile;
    }
    public boolean getProfile_background_tile(){
        return this.profile_background_tile;
    }
    public void setProfile_image_url(String profile_image_url){
        this.profile_image_url = profile_image_url;
    }
    public String getProfile_image_url(){
        return this.profile_image_url;
    }
    public void setProfile_image_url_https(String profile_image_url_https){
        this.profile_image_url_https = profile_image_url_https;
    }
    public String getProfile_image_url_https(){
        return this.profile_image_url_https;
    }
    public void setProfile_banner_url(String profile_banner_url){
        this.profile_banner_url = profile_banner_url;
    }
    public String getProfile_banner_url(){
        return this.profile_banner_url;
    }
    public void setProfile_link_color(String profile_link_color){
        this.profile_link_color = profile_link_color;
    }
    public String getProfile_link_color(){
        return this.profile_link_color;
    }
    public void setProfile_sidebar_border_color(String profile_sidebar_border_color){
        this.profile_sidebar_border_color = profile_sidebar_border_color;
    }
    public String getProfile_sidebar_border_color(){
        return this.profile_sidebar_border_color;
    }
    public void setProfile_sidebar_fill_color(String profile_sidebar_fill_color){
        this.profile_sidebar_fill_color = profile_sidebar_fill_color;
    }
    public String getProfile_sidebar_fill_color(){
        return this.profile_sidebar_fill_color;
    }
    public void setProfile_text_color(String profile_text_color){
        this.profile_text_color = profile_text_color;
    }
    public String getProfile_text_color(){
        return this.profile_text_color;
    }
    public void setProfile_use_background_image(boolean profile_use_background_image){
        this.profile_use_background_image = profile_use_background_image;
    }
    public boolean getProfile_use_background_image(){
        return this.profile_use_background_image;
    }
    public void setHas_extended_profile(boolean has_extended_profile){
        this.has_extended_profile = has_extended_profile;
    }
    public boolean getHas_extended_profile(){
        return this.has_extended_profile;
    }
    public void setDefault_profile(boolean default_profile){
        this.default_profile = default_profile;
    }
    public boolean getDefault_profile(){
        return this.default_profile;
    }
    public void setDefault_profile_image(boolean default_profile_image){
        this.default_profile_image = default_profile_image;
    }
    public boolean getDefault_profile_image(){
        return this.default_profile_image;
    }
    public void setFollowing(boolean following){
        this.following = following;
    }
    public boolean getFollowing(){
        return this.following;
    }
    public void setFollow_request_sent(boolean follow_request_sent){
        this.follow_request_sent = follow_request_sent;
    }
    public boolean getFollow_request_sent(){
        return this.follow_request_sent;
    }
    public void setNotifications(boolean notifications){
        this.notifications = notifications;
    }
    public boolean getNotifications(){
        return this.notifications;
    }
    public void setTranslator_type(String translator_type){
        this.translator_type = translator_type;
    }
    public String getTranslator_type(){
        return this.translator_type;
    }
}
