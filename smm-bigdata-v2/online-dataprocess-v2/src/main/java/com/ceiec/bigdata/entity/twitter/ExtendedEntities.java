package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/28.
 */
public class ExtendedEntities {
    private List<MediaExtend> media;

    public void setMedia(List<MediaExtend> media){
        this.media = media;
    }
    public List<MediaExtend> getMedia(){
        return this.media;
    }

    @Override
    public String toString() {
        return "{" +
                "media=" + media +
                '}';
    }
}
