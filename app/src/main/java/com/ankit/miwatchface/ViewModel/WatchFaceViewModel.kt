package com.ankit.miwatchface.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ankit.miwatchface.Model.WatchFaceModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class WatchFaceViewModel(var app: Application) : AndroidViewModel(app) {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    private val watchFaceRepository: WatchFaceRepository
    val watchFaceListLiveData: MutableLiveData<List<WatchFaceModel>> = MutableLiveData()

    init {
        watchFaceRepository = WatchFaceRepository()
       // watchFaceListLiveData = watchFaceRepository.watchFaceListLiveData
    }

    fun makeSilentApiHit() {
        watchFaceRepository.fetchDataFromApi();
    }

    fun fetchDataFromFirebase() {
        val database = Firebase.database
        val myRef = database.getReference("data")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val watchFaceListData = dataSnapshot.getValue<List<WatchFaceModel>>()
                watchFaceListLiveData.value = watchFaceListData
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("error","FirebaseFailure")
            }
        })
    }
}
