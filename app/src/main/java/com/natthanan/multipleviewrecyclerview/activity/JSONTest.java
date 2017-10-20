//package com.natthanan.multipleviewrecyclerview.activity;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//import com.natthanan.multipleviewrecyclerview.R;
//import com.natthanan.multipleviewsrecyclerview.BaseViewHolder;
//import com.natthanan.multipleviewsrecyclerview.JSONToRecyclerView;
//import com.natthanan.multipleviewsrecyclerview.intf.DataChangedCallback;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class JSONTest extends AppCompatActivity implements DataChangedCallback{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jsontest);
//
//        new JSONToRecyclerView(this, loadJSONFromAsset());
//    }
//
//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("oldJsonTest.json");
//
//            int size = is.available();
//
//            byte[] buffer = new byte[size];
//
//            is.read(buffer);
//
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//
//    }
//
//    @Override
//    public void onDatachange(String tag, BaseViewHolder viewHolder, View view, Object data) {
//
//    }
//}
