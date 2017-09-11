package com.natthanan.multipleviewrecyclerview;

import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewHolderType;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;
@ViewHolderType(2)
@LayoutID(R.layout.header)
public class HeaderViewHolder extends BaseViewHolder {

    @ViewID(R.id.header)
    TextView header;

    @ViewID(R.id.item)
    TextView item;


    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data) {
        header.setText("Header");
        item.setText((String) data);
    }
}
