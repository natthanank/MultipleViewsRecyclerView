package com.natthanan.multipleviewrecyclerview;

import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by DELL on 09/09/2560.
 */

@LayoutID(R.layout.footer)
public class FooterViewHolder extends BaseViewHolder {

    @ViewID(R.id.item)
    TextView item;
    @ViewID(R.id.footer)
    TextView footer;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data) {

        item.setText((String) data);
        footer.setText("Footer");

    }
}
