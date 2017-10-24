package com.natthanan.multipleviewrecyclerview.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.activity.RecyclerViewTest;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by DELL on 29/08/2560.
 */
@LayoutID(R.layout.header)
public class HeaderViewHolder extends BaseViewHolder<String> {


    @ViewID(R.id.item)
    TextView item;


    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(String data, final String tag) {
        item.setText(data);
    }
}
