package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created by DELL on 09/09/2560.
 */

public abstract class Drag extends ItemTouchHelper.Callback implements ItemTouchHelperAdapter {
    private final BaseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;
    List viewDataModels;

    public Drag(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        layoutManager = recyclerView.getLayoutManager();
        adapter = (BaseAdapter) recyclerView.getAdapter();
        isDragEnabled = true;
        viewDataModels = getAdapter().getViewDataModels();

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((BaseAdapter)recyclerView.getAdapter()).getViewDataModels(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((BaseAdapter)recyclerView.getAdapter()).getViewDataModels(), i, i - 1);
            }
        }
        (recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
        onItemMove(fromPosition, toPosition, (ViewDataModel) viewDataModels.get(fromPosition), (ViewDataModel) viewDataModels.get(toPosition));
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isDragEnabled();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public boolean isDragEnabled() {
        return isDragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        isDragEnabled = dragEnabled;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onSwipedLeft(int position, ViewDataModel viewDataModel) {

    }

    @Override
    public void onSwipeDown(int position, ViewDataModel viewDataModel) {

    }

    @Override
    public void onSwipeUp(int position, ViewDataModel viewDataModel) {

    }

    @Override
    public void onSwipedRight(int position, ViewDataModel viewDataModel) {

    }
}
