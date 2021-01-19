package com.nelyan.modals;
public class EventModel {

    int img;
    String eventName;
    String eventLocation;
    String eventDate;
    String eventTime;
    String eventTimeSecond;
    double eventPrice;
    String  eventDesc;

    public EventModel(int img, String eventName, String eventLocation, String eventDate, String eventTime, String eventTimeSecond, double eventPrice, String eventDesc) {
        this.img = img;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventTimeSecond = eventTimeSecond;
        this.eventPrice = eventPrice;
        this.eventDesc = eventDesc;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTimeSecond() {
        return eventTimeSecond;
    }

    public void setEventTimeSecond(String eventTimeSecond) {
        this.eventTimeSecond = eventTimeSecond;
    }

    public double getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(double eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}


