package com.natthanan.multipleviewrecyclerview.viewholder;

import android.renderscript.Double2;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.natthanan.multipleviewrecyclerview.R;
import com.natthanan.multipleviewrecyclerview.model.UserModel;
import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
import com.natthanan.multipleviewsrecyclerview.annotation.LayoutID;
import com.natthanan.multipleviewsrecyclerview.annotation.ViewID;

/**
 * Created by DELL on 29/08/2560.
 */
@LayoutID(R.layout.item)
public class ItemViewHolder extends BaseViewHolder {


    @ViewID(R.id.firstname)
    private TextView name;

    @ViewID(R.id.surname)
    private TextView surname;

    @ViewID(R.id.username)
    private TextView username;

    @ViewID(R.id.age)
    private TextView age;


    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object data, final String tag) {
        name.setText(((UserModel) data).getName());
        surname.setText(((UserModel) data).getSurname());
        age.setText("age : " + Double.valueOf(((UserModel) data).getAge()).intValue());
        username.setText(((UserModel) data).getUsername());
    }

}
