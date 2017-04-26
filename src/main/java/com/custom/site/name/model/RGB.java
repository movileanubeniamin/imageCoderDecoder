package com.custom.site.name.model;


public class RGB {
    private long r;
    private long g;
    private long b;

    public RGB() {
        super();
    }


    public RGB(long r, long g, long b) {
        super();
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public long getR() {
        return r;
    }


    public void setR(long r) {
        this.r = r;
    }


    public long getG() {
        return g;
    }


    public void setG(long g) {
        this.g = g;
    }


    public long getB() {
        return b;
    }


    public void setB(long b) {
        this.b = b;
    }


    @Override
    public String toString() {
        return "RGB [" + r + ", " + g + ", " + b + "]";
    }

}
