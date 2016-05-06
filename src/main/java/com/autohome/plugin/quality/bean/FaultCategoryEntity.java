package com.autohome.plugin.quality.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class FaultCategoryEntity<T> implements Serializable {
    String categoryid;
    String categoryname;
    String categorypercent;
    String url;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorypercent() {
        return categorypercent;
    }

    public void setCategorypercent(String categorypercent) {
        this.categorypercent = categorypercent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FaultCategoryEntity{" +
                "categoryid='" + categoryid + '\'' +
                ", categoryname='" + categoryname + '\'' +
                ", categorypercent='" + categorypercent + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
