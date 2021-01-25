package com.nelyan.modals;

import java.util.ArrayList;

public class DayTimeModel {

    ArrayList<Integer> selectTime;

    public void setSelectTime(ArrayList<Integer> selectTime) {
        this.selectTime = selectTime;
    }


    public ArrayList<Integer> getSelectTime() {
        return selectTime;
    }

    public DayTimeModel(ArrayList<Integer> selectTime) {
        this.selectTime = selectTime;
    }

}

