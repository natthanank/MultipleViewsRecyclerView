package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by DELL on 28/08/2560.
 */

public class ViewDataModel implements Cloneable {
    private int viewTypes;
    private Object model;
    private String tag;
    private BaseViewHolder baseViewHolderClass;
    private static RecyclerView recyclerView;
    private static SparseArray<Class<? extends BaseViewHolder>> baseViewHolderSparseArray = new SparseArray<>();

    public ViewDataModel(Class viewHolderClass, Object model) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
    }

    public ViewDataModel(Class viewHolderClass, Object model, String tag) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        setModel(model);
        setTag(tag);
    }

    public int getViewTypes() {
        return viewTypes;
    }

    public void setViewTypes(int viewTypes) {
        this.viewTypes = viewTypes;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public BaseViewHolder getBaseViewHolderClass() {
        return baseViewHolderClass;
    }

    public void setBaseViewHolderClass(BaseViewHolder baseViewHolderClass) {
        this.baseViewHolderClass = baseViewHolderClass;
    }

    private Integer getLayoutId(Class myClass) {
        Annotation annotation = myClass.getAnnotation(LayoutID.class);

        if (annotation instanceof LayoutID) {
            LayoutID layoutIdAnnotation = (LayoutID) annotation;
            return layoutIdAnnotation.value();
        }

        return null;
    }

    public BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> viewHolderClass, RecyclerView recyclerView) {
        try {
            Class<?> c = Class.forName(viewHolderClass.getName());
            Constructor<?> constructor = c.getConstructor(View.class);

            if (baseViewHolderSparseArray.get(baseViewHolderSparseArray.size()) == null) {
                baseViewHolderSparseArray.put(baseViewHolderSparseArray.size(), viewHolderClass);
            }

            int layout = getLayoutId(viewHolderClass);
            int type = baseViewHolderSparseArray.indexOfValue(viewHolderClass);

            Object instance = constructor.newInstance(inflateView(layout, recyclerView));

            ((BaseViewHolder) instance).setType(type);
            ((BaseViewHolder) instance).setLayout(layout);
            setViewTypes(type);

            return  ((BaseViewHolder) instance);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public View inflateView(int layout, RecyclerView recyclerView) {
        return LayoutInflater.from(this.getRecyclerView().getContext()).inflate(layout, this.getRecyclerView(), false);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView mRecyclerView) {
        recyclerView = mRecyclerView;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
