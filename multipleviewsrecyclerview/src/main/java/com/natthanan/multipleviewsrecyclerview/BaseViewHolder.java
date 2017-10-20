package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by DELL on 28/08/2560.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    private int type;
    private int layout;
    private RecyclerView recyclerView;
    private BaseViewHolder viewHolder;
    protected View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        setViewHolder(this);
        setViewFields(this, itemView);
    }

    public abstract void bind(T data, String tag);

    public BaseViewHolder createViewHolder(ViewDataModel viewDataModel, int layout, RecyclerView recyclerView) {
        try {
            Class<?> c = Class.forName(this.getClass().getName());
            Constructor<?> constructor = c.getConstructor(View.class);
            Object instance = constructor.newInstance(viewDataModel.inflateView(layout, this.getClass()));
            ((BaseViewHolder) instance).setRecyclerView(recyclerView);
            return (BaseViewHolder) instance;
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    private void setViewFields(BaseViewHolder baseViewHolder, View view) {
        Field[] fields = baseViewHolder.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ViewID.class)) {
                field.setAccessible(true);
                ViewID viewIdAnnotation = field.getAnnotation(ViewID.class);
                try {
                    field.set(baseViewHolder, field.getType().cast(view.findViewById(viewIdAnnotation.value())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startDrag() {
        ((BaseAdapter) getRecyclerView().getAdapter()).getItemTouchHelper().startDrag(BaseViewHolder.this);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public BaseViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(BaseViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}