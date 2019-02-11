package com.ceiec.bigdata.entity.twitter.es;

/**
 * Created by heyichang on 2017/10/31.
 */
public class EntityJson {

    private String Word;

    private String type;

    private int end;

    private int start;

    private Double score;

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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
                ", type='" + type + '\'' +
                ", end=" + end +
                ", start=" + start +
                ", score=" + score +
                '}';
    }
}
