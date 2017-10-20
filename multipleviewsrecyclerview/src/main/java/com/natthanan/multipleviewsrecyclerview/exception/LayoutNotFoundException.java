package com.natthanan.multipleviewsrecyclerview.exception;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;

/**
 * Created by natthanan on 10/20/2017.
 */

public class LayoutNotFoundException extends RuntimeException {

    public LayoutNotFoundException(Class<? extends BaseViewHolder> baseViewHolder) {
        super("Please add @LayoutID(<layoutID>) annotation to your " + baseViewHolder.getSimpleName() + " class.");
    }
}
