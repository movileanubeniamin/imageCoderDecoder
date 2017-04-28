package com.custom.site.name.model;


public class YUV {
    private long y;
    private long u;
    private long v;


    public YUV() {
        super();
    }


    public YUV(long y, long u, long v) {
        super();
        this.y = y;
        this.u = u;
        this.v = v;
    }


    public long getY() {
        return y;
    }


    public void setY(long y) {
        this.y = y;
    }


    public long getU() {
        return u;
    }


    public void setU(long u) {
        this.u = u;
    }


    public long getV() {
        return v;
    }


    public void setV(long v) {
        this.v = v;
    }


    @Override
    public String toString() {
        return "YUV [" + y + ", " + u + ", " + v + "]";
    }

}
