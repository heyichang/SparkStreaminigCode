package com.ceiec.bigdata.entity.twitter.es;

/**
 * Created by heyichang on 2017/10/31.
 */
public class Root {
    private String _index;

    private String _type;

    private String _id;

    private double _score;

    private Source _source;

    public void set_index(String _index){
        this._index = _index;
    }
    public String get_index(){
        return this._index;
    }
    public void set_type(String _type){
        this._type = _type;
    }
    public String get_type(){
        return this._type;
    }
    public void set_id(String _id){
        this._id = _id;
    }
    public String get_id(){
        return this._id;
    }
    public void set_score(double _score){
        this._score = _score;
    }
    public double get_score(){
        return this._score;
    }

    public Source get_source() {
        return _source;
    }

    public void set_source(Source _source) {
        this._source = _source;
    }

    @Override
    public String toString() {
        return "{" +
                "_index='" + _index + '\'' +
                ", _type='" + _type + '\'' +
                ", _id='" + _id + '\'' +
                ", _score=" + _score +
                ", _source=" + _source +
                '}';
    }
}
