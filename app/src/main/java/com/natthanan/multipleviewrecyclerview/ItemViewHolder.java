package com.natthanan.multipleviewrecyclerview;

import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;


@LayoutID(R.layout.item)
public class ItemViewHolder extends BaseViewHolder {

    @ViewID(R.id.itemlist)
    private TextView item;

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data) {
        item.setText((String) data);
    }


}
