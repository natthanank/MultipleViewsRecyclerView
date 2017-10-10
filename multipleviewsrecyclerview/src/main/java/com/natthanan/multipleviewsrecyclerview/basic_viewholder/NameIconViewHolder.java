package com.natthanan.multipleviewsrecyclerview.basic_viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by natthanan on 10/10/2017.
 */
@LayoutID(2130968630)
public class NameIconViewHolder extends BaseViewHolder {
    @ViewID(2131558537)
    TextView name;

    @ViewID(2131558480)
    ImageView icon;

    public NameIconViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, String tag) {

    }
}
