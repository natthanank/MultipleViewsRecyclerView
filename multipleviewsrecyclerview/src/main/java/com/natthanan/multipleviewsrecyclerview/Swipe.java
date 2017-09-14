package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by DELL on 29/08/2560.
 */

public abstract class Swipe extends ItemTouchHelper.Callback implements ItemTouchHelperAdapter{
    private final BaseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isSwipeEnabled;
    private int swipeFlags;
    private ItemTouchHelper itemTouchHelper;
    private Paint paint;
    private int movementFlags;
    private ViewDataModel viewDataModel;
    private ViewDataModel oldViewDataModel;
    private int lastIndex = -1;

    public Swipe(RecyclerView recyclerView, int movementFlags) {
        this.recyclerView = recyclerView;
        adapter = (BaseAdapter) recyclerView.getAdapter();
        layoutManager = recyclerView.getLayoutManager();
        isSwipeEnabled = true;
        this.movementFlags = movementFlags;

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        paint = new Paint();
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
        }
        return makeMovementFlags(0, movementFlags & swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final BaseAdapter baseAdapter = (BaseAdapter) recyclerView.getAdapter();
        viewDataModel = (ViewDataModel) baseAdapter.getViewDataModels().get(viewHolder.getAdapterPosition());
        try {
            setOldViewDataModel((ViewDataModel) viewDataModel.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (direction == ItemTouchHelper.START | direction == ItemTouchHelper.END) {
            try {
                setOldViewDataModel((ViewDataModel) viewDataModel.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        lastIndex = viewHolder.getAdapterPosition();
        switch (direction) {
            case ItemTouchHelper.LEFT:
                onSwipedLeft(viewHolder.getAdapterPosition(), viewDataModel);
                break;
            case ItemTouchHelper.RIGHT:
                onSwipedRight(viewHolder.getAdapterPosition(), viewDataModel);
                break;
            case ItemTouchHelper.UP:
                onSwipeUp(viewHolder.getAdapterPosition(), viewDataModel);
                break;
            case ItemTouchHelper.DOWN:
                onSwipeDown(viewHolder.getAdapterPosition(), viewDataModel);
                break;
            default:
        }
    }

    public void undoRemove(int position, ViewDataModel oldViewDataModel) {
        if (lastIndex==-1) return;
        getAdapter().getViewDataModels().add(lastIndex, getOldViewDataModel());
        getAdapter().notifyItemInserted(lastIndex);
        lastIndex=-1;
        viewDataModel=null;
    }

    public void undoUpdate(int position, ViewDataModel oldViewDataModel) {
        if (lastIndex==-1) return;
        getAdapter().getViewDataModels().set(lastIndex, oldViewDataModel);
        getAdapter().notifyItemChanged(lastIndex);
        lastIndex=-1;
        viewDataModel=null;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel) {
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isSwipeEnabled();
    }


    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }


    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public ViewDataModel getOldViewDataModel() {
        return oldViewDataModel;
    }

    public void setOldViewDataModel(ViewDataModel oldViewDataModel) {
        this.oldViewDataModel = oldViewDataModel;
    }
}
