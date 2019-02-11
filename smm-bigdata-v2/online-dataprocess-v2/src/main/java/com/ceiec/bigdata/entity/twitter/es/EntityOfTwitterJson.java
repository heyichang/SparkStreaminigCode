package com.ceiec.bigdata.entity.twitter.es;

import java.util.List;

/**
 * Created by heyichang on 2017/10/31.
 */
public class EntityOfTwitterJson {
    private String Word;

    private List<Integer> indices;

    public void setWord(String Word){
        this.Word = Word;
    }
    public String getWord(){
        return this.Word;
    }
    public void setIndices(List<Integer> indices){
        this.indices = indices;
    }
    public List<Integer> getIndices(){
        return this.indices;
    }
}
