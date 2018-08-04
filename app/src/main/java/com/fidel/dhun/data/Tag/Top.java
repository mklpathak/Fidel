
package com.fidel.dhun.data.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Top {

    @SerializedName("toptags")
    @Expose
    private Toptags toptags;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Top() {
    }

    /**
     * 
     * @param toptags
     */
    public Top(Toptags toptags) {
        super();
        this.toptags = toptags;
    }

    public Toptags getToptags() {
        return toptags;
    }

    public void setToptags(Toptags toptags) {
        this.toptags = toptags;
    }

}
