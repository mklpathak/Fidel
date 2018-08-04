package com.fidel.dhun.data;

/**
 * Created by fidel on 11/26/17.
 */

public class Tagforrecylerview
{


    private Integer count;

    private String name;

    private String url;



    public Tagforrecylerview(Integer count, String name, String url) {
        super();
        this.count = count;
        this.name = name;
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}