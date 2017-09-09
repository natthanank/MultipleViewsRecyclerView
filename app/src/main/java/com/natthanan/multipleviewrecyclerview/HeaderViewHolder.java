package com.natthanan.multipleviewrecyclerview;

import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by DELL on 09/09/2560.
 */

public class HeaderViewHolder extends BaseViewHolder {

    @ViewID(R.id.header)
    TextView header;

    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data) {
        header.setText((String) data);
    }
}
