package com.natthanan.multipleviewsrecyclerview.intf;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by natthanan on 9/22/2017.
 */

public interface iCallback {
    void onDatachange(String tag, int position, View view, Object data);
}
