package com.ankit.miwatchface.Retrofit;

import com.ankit.miwatchface.Model.WatchFaceListModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("everything")
    Call<WatchFaceListModel> getWatchFaceData();

}
