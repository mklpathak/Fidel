
package com.fidel.dhun.data.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Toptags implements Serializable
{

    @SerializedName("tag")
    @Expose
    private List<Tag> tag = null;
    @SerializedName("@attr")
    @Expose
    private Attr attr;
    private final static long serialVersionUID = -7314878104597247670L;

    /**
     * No args constructor for use in serialization
     * 
     */


    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

}
