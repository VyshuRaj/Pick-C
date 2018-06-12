package com.prts.pickcustomer.userrating;

/**
 * Created by satya on 30-Dec-17.
 */

public class Feedback {
    String text;
    boolean isSelected;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
