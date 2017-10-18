package com.natthanan.multipleviewrecyclerview;

import com.natthanan.multipleviewrecyclerview.model.RequestModel;
import com.natthanan.multipleviewrecyclerview.model.ResponseModel;
import com.natthanan.multipleviewrecyclerview.model.User;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by natthanan on 10/17/2017.
 */

public interface OnNetworkCallbackListener {
    public void onResponse(RequestModel responseModel, Retrofit retrofit);
    public void onBodyError(ResponseBody responseBodyError);
    public void onBodyErrorIsNull();
    public void onFailure(Throwable t);
}
