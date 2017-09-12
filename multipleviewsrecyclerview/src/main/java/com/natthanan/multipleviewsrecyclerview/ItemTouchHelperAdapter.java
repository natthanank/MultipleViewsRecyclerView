package com.natthanan.multipleviewsrecyclerview;

/**
 * Created by DELL on 29/08/2560.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);// ,Object data, Class Class

    void onSwipedRight(int position, ViewDataModel viewDataModel);
    void onSwipedLeft(int position, ViewDataModel viewDataModel);
    void onSwipeUp(int position, ViewDataModel viewDataModel);
    void onSwipeDown(int position, ViewDataModel viewDataModel);
}
