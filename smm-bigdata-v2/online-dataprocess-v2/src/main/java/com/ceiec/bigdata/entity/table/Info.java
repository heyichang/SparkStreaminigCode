package com.ceiec.bigdata.entity.table;

import com.ceiec.bigdata.entity.twitter.TwitterRoot;
import com.ceiec.bigdata.util.TimeUtils;


/**
 * Created by heyichang on 2017/10/13.
 */
public class Info {
    private TwitterRoot examplesRoot;
    private String InfoId;
    private String CreatingTIme;
    private String GatherTime;
    private String SensitiveId;
    private String Content;
    private String Summary;
    private String Title;

    private String SourceContent;
    private String SourceUrl;
    private String HotWordJson;
    private String EntityJson;
    private String EntityOfTwitterJson;
    private String SiteId;
    private String SiteTypeId;

    private String RemarkNumber;
    private String RetweetNumber;
    private String LikeNumber;
    private String AccountId;
    private String FirstLevalTypeId;
    private String SecondLevalTypeId;
    private String IsRetweet;

    private String RetweetInfoId;
    private String Longitude;
    private String Latitude;
    private String RegionId;
    private String Language;
    private String EmotionId;
    private String CreatingDate;
    private String GatherDate;



    public Info(TwitterRoot examplesRoot){
                this.examplesRoot=examplesRoot;

    }


    public String getInfoId() {

        return examplesRoot.getRowKey();
    }

    public void setInfoId(String infoId) {
        InfoId = infoId;
    }

    public String getCreatingTIme() {
        return TimeUtils.getCreatingTime( examplesRoot.getCreated_at());
    }

    public void setCreatingTIme(String creatingTIme) {
        CreatingTIme = creatingTIme;
    }

    public String getGatherTime() {
        return TimeUtils.getTime();
    }

    public void setGatherTime(String gatherTime) {
        GatherTime = gatherTime;
    }

    public String getSensitiveId() {

        return  SensitiveId="1";
    }

    public void setSensitiveId(String sensitiveId) {
        SensitiveId = sensitiveId;
    }

    public String getContent() {
        return examplesRoot.getText();
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSummary() {

        return Summary="1";
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getTitle() {

        return Title= "1";
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSourceContent() {
        return SourceContent="1";
    }

    public void setSourceContent(String sourceContent) {
        SourceContent = sourceContent;
    }

    public String getSourceUrl() {
        return SourceUrl="1";
    }

    public void setSourceUrl(String sourceUrl) {
        SourceUrl = sourceUrl;
    }

    public String getHotWordJson() {
        return HotWordJson="1";
    }

    public void setHotWordJson(String hotWordJson) {
        HotWordJson = hotWordJson;
    }

    public String getEntityJson() {
        return examplesRoot.getEntities().toString();
    }

    public void setEntityJson(String entityJson) {
        EntityJson = entityJson;
    }

    public String getEntityOfTwitterJson() {
        return "1";
    }

    public void setEntityOfTwitterJson(String entityOfTwitterJson) {
        EntityOfTwitterJson = entityOfTwitterJson;
    }

    public String getSiteId() {
        return SiteId="1";
    }

    public void setSiteId(String siteId) {
        SiteId = siteId;
    }

    public String getSiteTypeId() {
        return SiteTypeId="1";
    }

    public void setSiteTypeId(String siteTypeId) {
        SiteTypeId = siteTypeId;
    }

    public String getRemarkNumber() {
        return RemarkNumber="1";
    }

    public void setRemarkNumber(String remarkNumber) {
        RemarkNumber = remarkNumber;
    }

    public String getRetweetNumber() {
        return RetweetNumber="1";
    }

    public void setRetweetNumber(String retweetNumber) {
        RetweetNumber = retweetNumber;
    }

    public String getLikeNumber() {
        return LikeNumber="1";
    }

    public void setLikeNumber(String likeNumber) {
        LikeNumber = likeNumber;
    }

    public String getAccountId() {
        return AccountId="1";
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getFirstLevalTypeId() {
        return FirstLevalTypeId="1";
    }

    public void setFirstLevalTypeId(String firstLevalTypeId) {
        FirstLevalTypeId = firstLevalTypeId;
    }

    public String getSecondLevalTypeId() {
        return SecondLevalTypeId="1";
    }

    public void setSecondLevalTypeId(String secondLevalTypeId) {
        SecondLevalTypeId = secondLevalTypeId;
    }

    public String getIsRetweet() {
        return "1";
    }

    public void setIsRetweet(String isRetweet) {
        IsRetweet = isRetweet;
    }

    public String getRetweetInfoId() {
        return RetweetInfoId="1";
    }

    public void setRetweetInfoId(String retweetInfoId) {
        RetweetInfoId = retweetInfoId;
    }

    public String getLongitude() {
        return Longitude="1";
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude="1";
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getRegionId() {
        return RegionId="1";
    }

    public void setRegionId(String regionId) {
        RegionId = regionId;
    }

    public String getLanguage() {
        return Language="1";
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getEmotionId() {
        return EmotionId="1";
    }

    public void setEmotionId(String emotionId) {
        EmotionId = emotionId;
    }

    public String getCreatingDate() {
        return TimeUtils.getCreatingDate( examplesRoot.getCreated_at());
    }

    public void setCreatingDate(String creatingDate) {
        CreatingDate = creatingDate;
    }

    public String getGatherDate() {
        return TimeUtils.getDate();
    }

    public void setGatherDate(String gatherDate) {
        GatherDate = gatherDate;
    }

    @Override
    public String toString() {
        return "Info{" +
                "InfoId=" + InfoId +
                ", CreatingTIme='" + CreatingTIme + '\'' +
                ", GatherTime='" + GatherTime + '\'' +
                ", SensitiveId='" + SensitiveId + '\'' +
                ", Content='" + Content + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Title='" + Title + '\'' +
                ", SourceContent='" + SourceContent + '\'' +
                ", SourceUrl='" + SourceUrl + '\'' +
                ", HotWordJson='" + HotWordJson + '\'' +
                ", EntityJson='" + EntityJson + '\'' +
                ", EntityOfTwitterJson='" + EntityOfTwitterJson + '\'' +
                ", SiteId='" + SiteId + '\'' +
                ", SiteTypeId='" + SiteTypeId + '\'' +
                ", RemarkNumber='" + RemarkNumber + '\'' +
                ", RetweetNumber=" + RetweetNumber +
                ", LikeNumber=" + LikeNumber +
                ", AccountId='" + AccountId + '\'' +
                ", FirstLevalTypeId='" + FirstLevalTypeId + '\'' +
                ", SecondLevalTypeId='" + SecondLevalTypeId + '\'' +
                ", IsRetweet='" + IsRetweet + '\'' +
                ", RetweetInfoId='" + RetweetInfoId + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", RegionId='" + RegionId + '\'' +
                ", Language='" + Language + '\'' +
                ", EmotionId='" + EmotionId + '\'' +
                ", CreatingDate='" + CreatingDate + '\'' +
                ", GatherDate='" + GatherDate + '\'' +
                '}';
    }
}
