package com.jyyl.jinyou.http;

import java.util.List;

/**
 * 封装返回的 json
 * values : 对象数组
 * @param <T>
 */
public class HttpResult<T> {

    private String status;
    private String descritpion;
    private List<T> values;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "status='" + status + '\'' +
                ", descritpion='" + descritpion + '\'' +
                ", values=" + values.toString() +
                '}';
    }
}
