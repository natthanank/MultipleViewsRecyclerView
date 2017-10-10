package com.natthanan.multipleviewsrecyclerview.basic_viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.Swipe;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by natthanan on 10/10/2017.
 */
@LayoutID(2130968635)
public class NameIconDescriptionSwitchViewHolder extends BaseViewHolder {
    @ViewID(2131558537)
    TextView name;

    @ViewID(2131558480)
    ImageView icon;

    @ViewID(2131558541)
    TextView description;

    @ViewID(2131558539)
    Switch aSwitch;

    public NameIconDescriptionSwitchViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, String tag) {

    }
}
