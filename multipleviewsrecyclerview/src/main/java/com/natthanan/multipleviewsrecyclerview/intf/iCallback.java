package com.natthanan.multipleviewsrecyclerview.intf;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;

/**
 * Created by natthanan on 9/22/2017.
 */

public interface iCallback {
    void onDatachange(String tag, BaseViewHolder viewHolder, View view, Object data);
}
