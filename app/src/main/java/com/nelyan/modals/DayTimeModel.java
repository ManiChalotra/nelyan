package com.nelyan.modals;

import java.util.ArrayList;

public class DayTimeModel {

    ArrayList<TimeModel> selectTime;

    public void setSelectTime(ArrayList<TimeModel> selectTime) {
        this.selectTime = selectTime;
    }


    public ArrayList<TimeModel> getSelectTime() {
        return selectTime;
    }

    public DayTimeModel(ArrayList<TimeModel> selectTime) {
        this.selectTime = selectTime;
    }

}

