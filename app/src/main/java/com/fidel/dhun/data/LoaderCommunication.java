package com.fidel.dhun.data;

/**
 * Created by fidel on 12/1/17.
 */

public class LoaderCommunication {
   private int totalcalls;
   private int currentcalls;

    public LoaderCommunication(int totalcalls, int currentcalls) {
        this.totalcalls = totalcalls;
        this.currentcalls = currentcalls;
    }

    public int getTotalcalls() {
        return totalcalls;
    }

    public void setTotalcalls(int totalcalls) {
        this.totalcalls = totalcalls;
    }

    public int getCurrentcalls() {
        return currentcalls;
    }

    public void setCurrentcalls(int currentcalls) {
        this.currentcalls = currentcalls;
    }
}
