package com.natthanan.multipleviewsrecyclerview;

import com.natthanan.multipleviewsrecyclerview.annotation.ViewHolderType;

import java.lang.annotation.Annotation;

/**
 * Created by DELL on 28/08/2560.
 */

public class ViewDataModel {
    private int viewTypes;
    private Object model;

    public ViewDataModel(Class<? extends BaseViewHolder> viewHolderClass, Object model) {
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
}
