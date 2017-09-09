package com.natthanan.multipleviewsrecyclerview;

/**
 * Created by DELL on 29/08/2560.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
