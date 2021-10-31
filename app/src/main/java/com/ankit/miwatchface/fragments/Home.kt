package com.ankit.miwatchface.fragments

import android.Manifest
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ankit.miwatchface.Adapter.WatchFaceAdapter
import com.ankit.miwatchface.R
import com.ankit.miwatchface.ViewModel.WatchFaceViewModel
import com.ankit.miwatchface.WfContract

class Home : Fragment() , WfContract {

    val shouldFetchFromFirebase = true;
    lateinit var rvWatchFace: RecyclerView
    lateinit var cvNoInternet: CardView
    lateinit var watchFaceAdapter: WatchFaceAdapter
    lateinit var watchFaceViewModel: WatchFaceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        initRecyclerView()
        apiSilentHit()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }



    private fun initRecyclerView() {
        rvWatchFace.adapter = watchFaceAdapter
        watchFaceViewModel.watchFaceListLiveData.observe(
            viewLifecycleOwner,
            Observer { watchFaceModelList ->
                watchFaceModelList?.let {
                        watchFaceAdapter.updateList(watchFaceModelList)
                }
            })
    }

    private fun apiSilentHit() {

        if (!isNetworkAvailable()) {
            cvNoInternet.visibility = View.VISIBLE
        } else watchFaceViewModel.fetchDataFromFirebase()

    }

    private fun init(view: View) {
        rvWatchFace = view.findViewById(R.id.rvWatchFaces)
        cvNoInternet = view.findViewById(R.id.cvNoInternetLayout)
        watchFaceAdapter=WatchFaceAdapter(context!!)
        watchFaceAdapter.setContract(this)
        watchFaceViewModel = ViewModelProvider(this).get(WatchFaceViewModel::class.java)
    }

    fun stopLoading() {
        watchFaceAdapter?.stopLoading()
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(
                    String.format(
                        "package:%s",
                        context!!.applicationContext.packageName
                    )
                )
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                activity!!.parent,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                2300
            )
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = Home()
    }
}