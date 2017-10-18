package com.natthanan.multipleviewrecyclerview;

import com.natthanan.multipleviewrecyclerview.model.RequestModel;
import com.natthanan.multipleviewrecyclerview.model.ResponseModel;
import com.natthanan.multipleviewrecyclerview.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by natthanan on 10/17/2017.
 */

public class NetworkConnectionManager {
    public NetworkConnectionManager() {

    }

//    public void callServer(final OnNetworkCallbackListener listener,String username) {
//
//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://arismktd.ar.co.th")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Service git = retrofit.create(Service.class);
//        Call call = git.getUser(username);
//        call.enqueue(new Callback<ResponseModel>() {
//
//
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                ResponseModel user = response.body();
//                if (user == null) {
//                    //404 or the response cannot be converted to User.
//                    ResponseBody responseBody = response.errorBody();
//                    if (responseBody != null) {
//                        listener.onBodyError(responseBody);
//                    } else {
//                        listener.onBodyErrorIsNull();
//                    }
//                } else {
//                    //200
//                    listener.onResponse(user, retrofit);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                listener.onFailure(t);
//            }
//        });
//    }

    public void callServer(final OnNetworkCallbackListener listener, final RequestModel requestModel){

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://arismktd.ar.co.th/USE001/MSignin/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);
        Call call = service.login(requestModel.getDeviceOwner(),
                requestModel.getAppName(),
                requestModel.getOs(),
                requestModel.getPassword(),
                requestModel.getAppVersion(),
                requestModel.getDeviceName(),
                requestModel.getUsername(),
                requestModel.getDeviceModel(),
                requestModel.getEmulatorVersion(),
                requestModel.getMacAddress());
        call.enqueue(new Callback<RequestModel>() {

            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                RequestModel requestModel1 = response.body();
                if (requestModel == null) {
                    //404 or the response cannot be converted to User.
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
                        listener.onBodyError(responseBody);
                        System.out.println("bodyerror");

                    } else {
                        listener.onBodyErrorIsNull();
                        System.out.println("null");

                    }
                 }
                else {
                    System.out.println("ok");
                    listener.onResponse(requestModel1, retrofit);
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                listener.onFailure(t);
                System.out.println("failure");
            }

        });

    }
}

