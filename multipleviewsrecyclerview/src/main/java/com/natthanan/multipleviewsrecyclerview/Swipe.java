package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        BaseAdapter baseAdapter = (BaseAdapter) recyclerView.getAdapter();
        ViewDataModel viewDataModel = (ViewDataModel) baseAdapter.getViewDataModels().get(viewHolder.getAdapterPosition());

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

    @Override
    public void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel) {
    }

    //    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//            View itemView = viewHolder.itemView;
//            if (((LinearLayoutManager)recyclerView.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
//
//                float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                float width = height / 3;
//
//                if (dX > 0) {
//                    paint.setColor(Color.parseColor("#388E3C"));
//                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
//                    c.drawRect(background, paint);
//                } else {
//                    paint.setColor(Color.parseColor("#D32F2F"));
//                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//                    c.drawRect(background, paint);
//                }
//            } else {
//                float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                float width = height / 3;
//                if (dY > 0) {
//                    paint.setColor(Color.parseColor("#388E3C"));
//                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), dY);
//                    c.drawRect(background, paint);
//                } else {
//                    paint.setColor(Color.parseColor("#D32F2F"));
//                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getBottom() + dY, (float) itemView.getRight(), (float) itemView.getBottom());
//                    c.drawRect(background, paint);
//
//                }
//            }
//        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//    }


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
}
