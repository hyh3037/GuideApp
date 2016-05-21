package com.jyyl.guideapp.http;

/**
 *
 * @param <T>
 */
public class HttpResult<T> {

    private String status;
    private String descritpion;
    private T values;

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

    public T getValues() {
        return values;
    }

    public void setValues(T values) {
        this.values = values;
    }
}
