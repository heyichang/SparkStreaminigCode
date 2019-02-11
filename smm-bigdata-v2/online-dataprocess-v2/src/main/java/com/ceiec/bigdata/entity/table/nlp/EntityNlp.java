package com.ceiec.bigdata.entity.table.nlp;

import com.ceiec.bigdata.entity.twitter.es.EntityJson;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;

import java.util.List;

/**
 * Created by heyichang on 2017/11/24.
 */
public class EntityNlp {
    private Integer category;
    private List<EntityJson> entities ;
    private List<HotWordJson> hotWord ;
    private Integer sentiment;
    private String summary;
    private String language;

    @Override
    public String toString() {
        return "{" +
                "category=" + category +
                ", entities=" + entities +
                ", hotWord=" + hotWord +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<EntityJson> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityJson> entities) {
        this.entities = entities;
    }

    public List<HotWordJson> getHotWord() {
        return hotWord;
    }

    public void setHotWord(List<HotWordJson> hotWord) {
        this.hotWord = hotWord;
    }

    public Integer getSentiment() {
        return sentiment;
    }

    public void setSentiment(Integer sentiment) {
        this.sentiment = sentiment;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
