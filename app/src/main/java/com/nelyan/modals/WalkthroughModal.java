package com.nelyan.modals;

public class WalkthroughModal {
    int imagslideid;
    String tv_walk;
    String tv_walkdesc;

    public WalkthroughModal(int imagslideid, String tv_walk, String tv_walkdesc) {
        this.imagslideid = imagslideid;
        this.tv_walk = tv_walk;
        this.tv_walkdesc = tv_walkdesc;
    }

    public int getImagslideid() {
        return imagslideid;
    }

    public void setImagslideid(int imagslideid) {
        this.imagslideid = imagslideid;
    }

    public String getTv_walk() {
        return tv_walk;
    }

    public void setTv_walk(String tv_walk) {
        this.tv_walk = tv_walk;
    }

    public String getTv_walkdesc() {
        return tv_walkdesc;
    }

    public void setTv_walkdesc(String tv_walkdesc) {
        this.tv_walkdesc = tv_walkdesc;
    }
}
