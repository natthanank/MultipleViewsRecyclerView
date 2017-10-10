package com.natthanan.multipleviewsrecyclerview.basic_viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseAdapter;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by natthanan on 10/10/2017.
 */
@LayoutID(2130968626)
public class NameDescriptionButtonViewHolder extends BaseViewHolder {
    @ViewID(2131558537)
    TextView name;

    @ViewID(2131558541)
    TextView description;

    @ViewID(2131558540)
    Button button;

    public NameDescriptionButtonViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, String tag) {

    }
}
