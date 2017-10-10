package com.natthanan.multipleviewsrecyclerview.basic_viewholder;

import android.view.View;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by natthanan on 10/10/2017.
 */
@LayoutID(2130968622)
public class NameViewHolder extends BaseViewHolder {

    @ViewID(2131558537)
    TextView name;
    public NameViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, String tag) {
        name.setText(((String) data));
    }
}
