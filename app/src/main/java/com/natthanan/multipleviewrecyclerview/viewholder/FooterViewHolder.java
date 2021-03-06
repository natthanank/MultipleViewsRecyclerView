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
 * Created by DELL on 09/09/2560.
 */


@LayoutID(R.layout.footer)
public class FooterViewHolder extends BaseViewHolder {

    @ViewID(R.id.item)
    public TextView item;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, final String tag) {
        item.setText(((String) data));
    }

}
