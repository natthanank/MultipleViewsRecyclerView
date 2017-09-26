package com.natthanan.multipleviewsrecyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DELL on 09/09/2560.
 */

public abstract class Drag extends ItemTouchHelper.Callback {
    private final BaseAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isDragEnabled;
    private int dragFlags;
    private ItemTouchHelper itemTouchHelper;
    List viewDataModels;
    List<Class<? extends BaseViewHolder>> viewHolderClasses;
    public static boolean isDrag = true;

    public Drag(RecyclerView recyclerView) {
        layoutManager = recyclerView.getLayoutManager();
        adapter = (BaseAdapter) recyclerView.getAdapter();
        adapter.setDrag(true);
        isDragEnabled = true;
        viewDataModels = getAdapter().getViewDataModels();
        viewHolderClasses = new ArrayList();

        itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setItemTouchHelper(itemTouchHelper);

    }

    public void addGroup(Class<? extends BaseViewHolder> viewHolderClass) {
        viewHolderClasses.add(viewHolderClass);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
        } else if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        }
        return makeMovementFlags(dragFlags, 0);
    }

    private boolean isGrouped(Class<? extends BaseViewHolder> viewHolderClass) {
        for (Class<? extends BaseViewHolder> viewHolder: viewHolderClasses){
            if (viewHolder == viewHolderClass) {
                return true;
            }
        }
        return false;
    }

    private boolean nextTargetIsSameType(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return ((BaseViewHolder)viewHolder).getClass() != ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition() + 1)).getBaseViewHolderClass().getClass();
    }

    private boolean prevTargetIsSameType(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return ((BaseViewHolder)viewHolder).getClass() != ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition() - 1)).getBaseViewHolderClass().getClass();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        isDrag = true;
        try {
            // normal case
            if (fromPosition < toPosition) {
                // check viewholder class match the group
                if ((isGrouped(((BaseViewHolder) target).getClass()))
                        // check viewholder class match the target class
                        && (target.getClass() != viewHolder.getClass())) {
                    // check current target match the next target
                    if (isCurrentTargetTypeMatchNextTarget(target)) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i + 1);
                        }
                        (recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
                        onItemMove(fromPosition, toPosition, (ViewDataModel) viewDataModels.get(fromPosition), (ViewDataModel) viewDataModels.get(toPosition));
                        return true;
                    }
                    return false;
                }
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i + 1);
                }
            } else {
                if ((isGrouped(((BaseViewHolder) target).getClass()))
                        && (target.getClass() != viewHolder.getClass())
//                        && (prevTargetIsSameType(viewHolder, target))
                        ) {
                    if (isCurrentTargetTypeMatchPrevTarget(target)) {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i - 1);
                        }
                        (recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
                        onItemMove(fromPosition, toPosition, (ViewDataModel) viewDataModels.get(fromPosition), (ViewDataModel) viewDataModels.get(toPosition));
                        return true;
                    }
                    return false;
                }
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i - 1);
                }
            }
            (recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
            onItemMove(fromPosition, toPosition, (ViewDataModel) viewDataModels.get(fromPosition), (ViewDataModel) viewDataModels.get(toPosition));
            return true;
        } catch (IndexOutOfBoundsException e) {
            // case toPosition=0 (swap with first item)
            if (toPosition == 0) {
                for (int i = fromPosition; i > 0; i--) {
                    Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i - 1);
                    (recyclerView.getAdapter()).notifyItemMoved(i, i - 1);
                }
            }
            // case toPosition=adapter.getViewDataModels().size()-1 (swap with last item)
            if (toPosition == adapter.getViewDataModels().size() -1) {
                for (int i = fromPosition; i < adapter.getViewDataModels().size() -1; i++) {
                    Collections.swap(((BaseAdapter) recyclerView.getAdapter()).getViewDataModels(), i, i + 1);
                    (recyclerView.getAdapter()).notifyItemMoved(i, i + 1);
                }
            }

            onItemMove(fromPosition, toPosition, (ViewDataModel) viewDataModels.get(fromPosition), (ViewDataModel) viewDataModels.get(toPosition));
            return true;
        }
    }

    private boolean isCurrentTargetTypeMatchNextTarget(RecyclerView.ViewHolder target) {
        return ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition())).getViewTypes() !=
                ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition() + 1)).getViewTypes();
    }

    private boolean isCurrentTargetTypeMatchPrevTarget(RecyclerView.ViewHolder target) {
        return ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition())).getViewTypes() !=
                ((ViewDataModel)adapter.getViewDataModels().get(target.getAdapterPosition() - 1)).getViewTypes();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isDragEnabled();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (isCurrentlyActive) {
            if (isDrag) {
                float alpha = 0.5f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        } else {
            viewHolder.itemView.setAlpha(1);
            viewHolder.itemView.setTranslationX(dX);
        }
    }

    public abstract void onItemMove(int fromPosition, int toPosition, ViewDataModel fromViewDataModel, ViewDataModel toViewDataModel);

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

}
