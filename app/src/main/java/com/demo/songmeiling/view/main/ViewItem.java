package com.demo.songmeiling.view.main;

import java.io.Serializable;

/**
 * Created by songmeiling on 2017/6/30.
 */
public class ViewItem implements Serializable {
    private String title;
    private String action;
    private String color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
