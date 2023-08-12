package com.example.gossip.Status;

public class StatusState {
    private String mobile;
    private boolean state;

    public StatusState(String mobile, boolean state) {
        this.mobile = mobile;
        this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
