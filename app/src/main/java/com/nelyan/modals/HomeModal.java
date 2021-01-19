package com.nelyan.modals;

public class HomeModal {
    int img;
    String task;

    public HomeModal(int img, String task) {
        this.img = img;
        this.task = task;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

