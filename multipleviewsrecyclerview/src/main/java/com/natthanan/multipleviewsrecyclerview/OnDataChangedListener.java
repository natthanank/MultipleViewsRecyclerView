package com.natthanan.multipleviewsrecyclerview;

import android.view.View;

/**
 * Created by natthanan on 9/19/2017.
 */

public interface OnDataChangedListener {

    Message onDataChanged(View view, String oldData, String newData);
}
