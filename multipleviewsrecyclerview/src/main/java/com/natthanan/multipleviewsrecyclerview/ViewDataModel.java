package com.natthanan.multipleviewsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewHolderType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by DELL on 28/08/2560.
 */

public class ViewDataModel {
    private int viewTypes;
    private Object model;
    private BaseViewHolder baseViewHolderClass;
    private static RecyclerView recyclerView;

    public ViewDataModel(Class<? extends BaseViewHolder> viewHolderClass, Object model) {
        setBaseViewHolderClass(createViewHolder(viewHolderClass, recyclerView));
        Annotation annotation = viewHolderClass.getAnnotation(ViewHolderType.class);
        if (annotation instanceof ViewHolderType) {
            ViewHolderType viewHolderTypeAnnotation = (ViewHolderType) annotation;
            setViewTypes(viewHolderTypeAnnotation.value());
        }
        setModel(model);


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

    private Integer getViewHolderType(Class myClass) {
        Annotation annotation = myClass.getAnnotation(ViewHolderType.class);

        if (annotation instanceof ViewHolderType) {
            ViewHolderType viewHolderTypeAnnotation = (ViewHolderType) annotation;
            return viewHolderTypeAnnotation.value();
        }

        return null;
    }

    public BaseViewHolder createViewHolder(Class<? extends BaseViewHolder> viewHolderClass, RecyclerView recyclerView) {
        try {
            Class<?> c = Class.forName(viewHolderClass.getName());
            Constructor<?> constructor = c.getConstructor(View.class);
            int layout = getLayoutId(viewHolderClass);
            int type = getViewHolderType(viewHolderClass);
            Object instance = constructor.newInstance(inflateView(layout, recyclerView));
            ((BaseViewHolder) instance).setType(type);
            ((BaseViewHolder) instance).setLayout(layout);
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
}
