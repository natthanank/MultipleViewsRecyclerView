package com.natthanan.multipleviewsrecyclerview.intf;

import com.natthanan.multipleviewsrecyclerview.ViewDataModel;

/**
 * Created by DELL on 29/08/2560.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel);

    void onSwipedRight(int position, ViewDataModel viewDataModel);
    void onSwipedLeft(int position, ViewDataModel viewDataModel);
    void onSwipeUp(int position, ViewDataModel viewDataModel);
    void onSwipeDown(int position, ViewDataModel viewDataModel);
    void onUpdateSwiped(int position, ViewDataModel viewDataModel, int action);
}