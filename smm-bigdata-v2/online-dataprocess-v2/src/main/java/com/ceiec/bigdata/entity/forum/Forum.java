package com.ceiec.bigdata.entity.forum;

/**
 * Created by heyichang on 2017/11/6.
 */
public class Forum {
    private String title;

    private String content;

    private String abstracts;

    private String html;

    private String gathertime;

    private String createtime;

    private String url;

    private String sitename;

    private String username;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setAbstracts(String abstracts){
        this.abstracts = abstracts;
    }
    public String getAbstracts(){
        return this.abstracts;
    }
    public void setHtml(String html){
        this.html = html;
    }
    public String getHtml(){
        return this.html;
    }
    public void setGathertime(String gathertime){
        this.gathertime = gathertime;
    }
    public String getGathertime(){
        return this.gathertime;
    }
    public void setCreatetime(String createtime){
        this.createtime = createtime;
    }
    public String getCreatetime(){
        return this.createtime;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setSitename(String sitename){
        this.sitename = sitename;
    }
    public String getSitename(){
        return this.sitename;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", abstracts='" + abstracts + '\'' +
                ", html='" + html + '\'' +
                ", gathertime='" + gathertime + '\'' +
                ", createtime='" + createtime + '\'' +
                ", url='" + url + '\'' +
                ", sitename='" + sitename + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
