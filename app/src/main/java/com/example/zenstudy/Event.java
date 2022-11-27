package com.example.zenstudy;

public class Event{

    String EVENT, DETAILS, startTIME, endTIME, firstDATE, secondDATE, NOTIFY;

    public Event(String EVENT, String DETAILS, String startTIME, String endTIME, String firstDATE, String secondDATE, String NOTIFY){
        this.EVENT = EVENT;
        this.DETAILS = DETAILS;
        this.startTIME = startTIME;
        this.endTIME = endTIME;
        this.firstDATE = firstDATE;
        this.secondDATE = secondDATE;
        this.NOTIFY = NOTIFY;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(String DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getStartTIME() {
        return startTIME;
    }

    public void setStartTIME(String startTIME) {
        this.startTIME = startTIME;
    }

    public String getEndTIME() {
        return endTIME;
    }

    public void setEndTIME(String endTIME) {
        this.endTIME = endTIME;
    }

    public String getFirstDATE() {
        return firstDATE;
    }

    public void setFirstDATE(String firstDATE) {
        this.firstDATE = firstDATE;
    }

    public String getSecondDATE() {
        return secondDATE;
    }

    public void setSecondDATE(String secondDATE) {
        this.secondDATE = secondDATE;
    }

    public String getNOTIFY() {
        return NOTIFY;
    }

    public void setNOTIFY(String NOTIFY) {
        this.NOTIFY = NOTIFY;
    }
}
