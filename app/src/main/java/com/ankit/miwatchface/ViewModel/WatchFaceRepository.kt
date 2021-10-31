package com.ankit.miwatchface.ViewModel

import androidx.lifecycle.MutableLiveData
import com.ankit.miwatchface.Model.WatchFaceListModel
import com.ankit.miwatchface.Retrofit.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchFaceRepository {

    var watchFaceListLiveData: MutableLiveData<WatchFaceListModel> = MutableLiveData()

    fun fetchDataFromApi() {

        var call = ApiClient.getInstance().api.watchFaceData

        call.enqueue(object : Callback<WatchFaceListModel> {

            override fun onFailure(call: Call<WatchFaceListModel>, t: Throwable?) {
            }

            override fun onResponse(
                call: Call<WatchFaceListModel>?,
                response: Response<WatchFaceListModel>?
            ) {

                if (response!!.isSuccessful && response.body()!!.data != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        watchFaceListLiveData.value = response.body()
                    }
                }
            }
        })
    }
}