package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/12/22.
 */
public class VideoInfo {

    private List<Variants> variants;

    private int duration_millis;

    private List<Integer> aspect_ratio;

    public List<Variants> getVariants() {
        return variants;
    }

    public void setVariants(List<Variants> variants) {
        this.variants = variants;
    }

    public int getDuration_millis() {
        return duration_millis;
    }

    public void setDuration_millis(int duration_millis) {
        this.duration_millis = duration_millis;
    }

    public List<Integer> getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(List<Integer> aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }
}
