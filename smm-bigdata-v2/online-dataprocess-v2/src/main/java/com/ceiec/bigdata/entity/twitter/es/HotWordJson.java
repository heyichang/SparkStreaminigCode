package com.ceiec.bigdata.entity.twitter.es;

/**
 * Created by heyichang on 2017/10/31.
 */
public class HotWordJson {
    private String Word;

    private Double score;

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "{" +
                "Word='" + Word + '\'' +
                ", score=" + score +
                '}';
    }
}
