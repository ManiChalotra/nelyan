package com.nelyan.modals;


public class DetailsTimeModal {
    String time;
    String year;
    String timeone;
    String yearone;

    public DetailsTimeModal(String time, String year, String timeone, String yearone) {
        this.time = time;
        this.year = year;
        this.timeone = timeone;
        this.yearone = yearone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTimeone() {
        return timeone;
    }

    public void setTimeone(String timeone) {
        this.timeone = timeone;
    }

    public String getYearone() {
        return yearone;
    }

    public void setYearone(String yearone) {
        this.yearone = yearone;
    }
}
