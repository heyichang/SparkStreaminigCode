package com.ceiec.bigdata.entity.table.nlp;

/**
 * Created by heyichang on 2017/11/24.
 */
public class NlpResult {
    EntityNlp result;

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                '}';
    }

    public EntityNlp getResult() {
        return result;
    }

    public void setResult(EntityNlp result) {
        this.result = result;
    }
}
