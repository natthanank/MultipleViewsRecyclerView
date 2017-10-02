package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by DELL on 29/08/2560.
 */

public abstract class Swipe extends ItemTouchHelper.Callback{
    public static final int ACTION_REMOVE = 0;
    public static final int ACTION_UPDATE = 1;

    private final BaseAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isSwipeEnabled;
    private int swipeFlags;
    private ItemTouchHelper itemTouchHelper;
    private int movementFlags;
    private ViewDataModel viewDataModel;
    private ViewDataModel oldViewDataModel;
    private boolean isUndo = false;
    private int action;
    private int duration = 3500;

    public Swipe(RecyclerView recyclerView, int movementFlags) {
        this.recyclerView = recyclerView;
        adapter = (BaseAdapter) recyclerView.getAdapter();
        layoutManager = recyclerView.getLayoutManager();
        isSwipeEnabled = true;
        this.movementFlags = movementFlags;

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        Drag.isDrag = false;
        isUndo = false;
        final int position = viewHolder.getAdapterPosition();
        final BaseAdapter baseAdapter = (BaseAdapter) recyclerView.getAdapter();
        viewDataModel = (ViewDataModel) baseAdapter.getViewDataModels().get(viewHolder.getAdapterPosition());
        try {
            setOldViewDataModel((ViewDataModel) viewDataModel.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        switch (direction) {
            case ItemTouchHelper.LEFT:
                onSwipedLeft(position, viewDataModel);
                break;
            case ItemTouchHelper.RIGHT:
                onSwipedRight(position, viewDataModel);
                break;
            case ItemTouchHelper.UP:
                onSwipeUp(position, viewDataModel);
                break;
            case ItemTouchHelper.DOWN:
                onSwipeDown(position, viewDataModel);
                break;
            default:
        }
        new CountDownTimer(getDuration(), getDuration()) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isUndo == false) {
                    onUpdateSwiped(position, viewDataModel, action);
                }
            }
        }.start();

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (isCurrentlyActive) {
            viewHolder.itemView.setAlpha(1);
            viewHolder.itemView.setTranslationX(dX);
        }
    }

    public void undoRemove(int position, ViewDataModel oldViewDataModel) {
        getAdapter().getViewDataModels().add(position, oldViewDataModel);
        getAdapter().notifyItemInserted(position);
        getRecyclerView().scrollToPosition(position);
        viewDataModel=null;
        isUndo = true;
    }

    public void undoUpdate(int position, ViewDataModel oldViewDataModel) {
        getAdapter().getViewDataModels().set(position, oldViewDataModel);
        getAdapter().notifyItemChanged(position);
        getRecyclerView().scrollToPosition(position);
        viewDataModel=null;
        isUndo = true;
    }

    public void removeItem(int position, ViewDataModel viewDataModel) {
        action = 0;
        getAdapter().getViewDataModels().remove(viewDataModel);
        getAdapter().notifyItemRemoved(position);
    }

    public void updateItem(int position, ViewDataModel viewDataModel) {
        action = 1;
        getAdapter().getViewDataModels().set(position, viewDataModel);
        getAdapter().notifyItemChanged(position);
    }

    public abstract void onSwipedRight(int position, ViewDataModel viewDataModel);
    public abstract void onSwipedLeft(int position, ViewDataModel viewDataModel);
    public abstract void onSwipeUp(int position, ViewDataModel viewDataModel);
    public abstract void onSwipeDown(int position, ViewDataModel viewDataModel);
    public abstract void onUpdateSwiped(int position, ViewDataModel viewDataModel, int action);

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
