package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by DELL on 09/09/2560.
 */

public abstract class Drag extends ItemTouchHelper.Callback {
    private final BaseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;

    public Drag(BaseAdapter adapter, RecyclerView recyclerView) {
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.layoutManager = recyclerView.getLayoutManager();
        this.isDragEnabled = true;

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
        updateDraggedItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        getAdapter().onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isDragEnabled();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public abstract void updateDraggedItem(int oldPosition, int newPosition);

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
}
