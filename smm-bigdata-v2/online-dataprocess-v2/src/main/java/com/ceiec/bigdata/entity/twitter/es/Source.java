package com.ceiec.bigdata.entity.twitter.es;

import java.util.List;

/**
 * Created by heyichang on 2017/10/31.
 */
public class Source {
    private String GatherDate;

    private int SecondLevalTypeId;

    private List<com.ceiec.bigdata.entity.twitter.es.EntityJson> EntityJson;

    private List<HotWordJson> HotWordJson;

    private String Content;

    private int SiteId;

    private int FirstLevalTypeId;

    private String Language;

    private List<EntityOfTwitterJson> EntityOfTwitterJson;

    private int RemarkNumber;

    private boolean IsRetweet;

    private int EmotionId;

    private int RegionId;

    private int SensitiveId;

    private int SiteTypeId;

    private String SourceUrl;

    private String Summary;

    private String CreatingTIme;

    private String SourceContent;

    private String GatherTime;

    private String Title;

    private int LikeNumber;

    private int RetweetNumber;

    private Location location;

    private String CreatingDate;

    private String InfoId;

    private String AccountId;

    public void setGatherDate(String GatherDate){
        this.GatherDate = GatherDate;
    }
    public String getGatherDate(){
        return this.GatherDate;
    }
    public void setSecondLevalTypeId(int SecondLevalTypeId){
        this.SecondLevalTypeId = SecondLevalTypeId;
    }
    public int getSecondLevalTypeId(){
        return this.SecondLevalTypeId;
    }
    public void setEntityJson(List<com.ceiec.bigdata.entity.twitter.es.EntityJson> EntityJson){
        this.EntityJson = EntityJson;
    }
    public List<com.ceiec.bigdata.entity.twitter.es.EntityJson> getEntityJson(){
        return this.EntityJson;
    }
    public void setHotWordJson(List<HotWordJson> HotWordJson){
        this.HotWordJson = HotWordJson;
    }
    public List<HotWordJson> getHotWordJson(){
        return this.HotWordJson;
    }
    public void setContent(String Content){
        this.Content = Content;
    }
    public String getContent(){
        return this.Content;
    }
    public void setSiteId(int SiteId){
        this.SiteId = SiteId;
    }
    public int getSiteId(){
        return this.SiteId;
    }
    public void setFirstLevalTypeId(int FirstLevalTypeId){
        this.FirstLevalTypeId = FirstLevalTypeId;
    }
    public int getFirstLevalTypeId(){
        return this.FirstLevalTypeId;
    }
    public void setLanguage(String Language){
        this.Language = Language;
    }
    public String getLanguage(){
        return this.Language;
    }
    public void setEntityOfTwitterJson(List<EntityOfTwitterJson> EntityOfTwitterJson){
        this.EntityOfTwitterJson = EntityOfTwitterJson;
    }
    public List<EntityOfTwitterJson> getEntityOfTwitterJson(){
        return this.EntityOfTwitterJson;
    }
    public void setRemarkNumber(int RemarkNumber){
        this.RemarkNumber = RemarkNumber;
    }
    public int getRemarkNumber(){
        return this.RemarkNumber;
    }
    public void setIsRetweet(boolean IsRetweet){
        this.IsRetweet = IsRetweet;
    }
    public boolean getIsRetweet(){
        return this.IsRetweet;
    }
    public void setEmotionId(int EmotionId){
        this.EmotionId = EmotionId;
    }
    public int getEmotionId(){
        return this.EmotionId;
    }
    public void setRegionId(int RegionId){
        this.RegionId = RegionId;
    }
    public int getRegionId(){
        return this.RegionId;
    }
    public void setSensitiveId(int SensitiveId){
        this.SensitiveId = SensitiveId;
    }
    public int getSensitiveId(){
        return this.SensitiveId;
    }
    public void setSiteTypeId(int SiteTypeId){
        this.SiteTypeId = SiteTypeId;
    }
    public int getSiteTypeId(){
        return this.SiteTypeId;
    }
    public void setSourceUrl(String SourceUrl){
        this.SourceUrl = SourceUrl;
    }
    public String getSourceUrl(){
        return this.SourceUrl;
    }
    public void setSummary(String Summary){
        this.Summary = Summary;
    }
    public String getSummary(){
        return this.Summary;
    }
    public void setCreatingTIme(String CreatingTIme){
        this.CreatingTIme = CreatingTIme;
    }
    public String getCreatingTIme(){
        return this.CreatingTIme;
    }
    public void setSourceContent(String SourceContent){
        this.SourceContent = SourceContent;
    }
    public String getSourceContent(){
        return this.SourceContent;
    }
    public void setGatherTime(String GatherTime){
        this.GatherTime = GatherTime;
    }
    public String getGatherTime(){
        return this.GatherTime;
    }
    public void setTitle(String Title){
        this.Title = Title;
    }
    public String getTitle(){
        return this.Title;
    }
    public void setLikeNumber(int LikeNumber){
        this.LikeNumber = LikeNumber;
    }
    public int getLikeNumber(){
        return this.LikeNumber;
    }
    public void setRetweetNumber(int RetweetNumber){
        this.RetweetNumber = RetweetNumber;
    }
    public int getRetweetNumber(){
        return this.RetweetNumber;
    }
    public void setLocation(Location location){
        this.location = location;
    }
    public Location getLocation(){
        return this.location;
    }
    public void setCreatingDate(String CreatingDate){
        this.CreatingDate = CreatingDate;
    }
    public String getCreatingDate(){
        return this.CreatingDate;
    }
    public void setInfoId(String InfoId){
        this.InfoId = InfoId;
    }
    public String getInfoId(){
        return this.InfoId;
    }
    public void setAccountId(String AccountId){
        this.AccountId = AccountId;
    }
    public String getAccountId(){
        return this.AccountId;
    }

    @Override
    public String toString() {
        return "{" +
                "GatherDate='" + GatherDate + '\'' +
                ", SecondLevalTypeId=" + SecondLevalTypeId +
                ", EntityJson=" + EntityJson +
                ", HotWordJson=" + HotWordJson +
                ", Content='" + Content + '\'' +
                ", SiteId=" + SiteId +
                ", FirstLevalTypeId=" + FirstLevalTypeId +
                ", Language='" + Language + '\'' +
                ", EntityOfTwitterJson=" + EntityOfTwitterJson +
                ", RemarkNumber=" + RemarkNumber +
                ", IsRetweet=" + IsRetweet +
                ", EmotionId=" + EmotionId +
                ", RegionId=" + RegionId +
                ", SensitiveId=" + SensitiveId +
                ", SiteTypeId=" + SiteTypeId +
                ", SourceUrl='" + SourceUrl + '\'' +
                ", Summary='" + Summary + '\'' +
                ", CreatingTIme='" + CreatingTIme + '\'' +
                ", SourceContent='" + SourceContent + '\'' +
                ", GatherTime='" + GatherTime + '\'' +
                ", Title='" + Title + '\'' +
                ", LikeNumber=" + LikeNumber +
                ", RetweetNumber=" + RetweetNumber +
                ", location=" + location +
                ", CreatingDate='" + CreatingDate + '\'' +
                ", InfoId='" + InfoId + '\'' +
                ", AccountId='" + AccountId + '\'' +
                '}';
    }
}
