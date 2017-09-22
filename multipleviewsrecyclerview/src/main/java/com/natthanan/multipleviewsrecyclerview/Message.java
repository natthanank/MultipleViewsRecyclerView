package com.natthanan.multipleviewsrecyclerview;

import android.view.View;

/**
 * Created by natthanan on 9/19/2017.
 */

public class Message {

    private View view;
    private String oldData;
    private String newData;
    private int position;

    public String getMessage() {
        String message = getView().getClass().getSimpleName() + " at position " + getPosition()  + " has changed  to " + getNewData();
        return message;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}