package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by DELL on 29/08/2560.
 */

public abstract class Swipe extends ItemTouchHelper.Callback implements ItemTouchHelperAdapter{
    public static int ACTION_REMOVE = 0;
    public static int ACTION_UPDATE = 1;

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
    private boolean isUndo = false;
    private int action;

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
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
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
        new CountDownTimer(3500, 3500) {
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


    public void undoRemove(int position, ViewDataModel oldViewDataModel) {
        getAdapter().getViewDataModels().add(position, oldViewDataModel);
        System.out.println(((ViewDataModel)getAdapter().getViewDataModels().get(position)).getModel());
        getAdapter().notifyItemInserted(position);
        viewDataModel=null;
        isUndo = true;
    }

    public void undoUpdate(int position, ViewDataModel oldViewDataModel) {
        getAdapter().getViewDataModels().set(position, oldViewDataModel);
        getAdapter().notifyItemChanged(position);
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
