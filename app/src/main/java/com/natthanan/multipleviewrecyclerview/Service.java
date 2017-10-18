package com.natthanan.multipleviewrecyclerview;

import com.natthanan.multipleviewrecyclerview.model.RequestModel;
import com.natthanan.multipleviewrecyclerview.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by natthanan on 10/17/2017.
 */

public interface Service {

    @FormUrlEncoded
    @POST("/USE001/MSignin")
    Call<RequestModel> login(@Field("tk_device_owner") String deviceOwner,
                             @Field("tk_app_name") String appName,
                             @Field("tk_os") String os,
                             @Field("password") String password,
                             @Field("tk_app_version") String appVersion,
                             @Field("tk_device_name") String deviceName,
                             @Field("username") String username,
                             @Field("tk_device_model") String deviceModel,
                             @Field("tk_emulator_version") String emulatorVersion,
                             @Field("tk_mac_address") String macAddress);

    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);

}
