package com.ankit.miwatchface.Adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ankit.miwatchface.Model.WatchFaceModel
import com.ankit.miwatchface.R
import com.ankit.miwatchface.WfContract
import com.squareup.picasso.Picasso
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


class WatchFaceAdapter(var context: Context) :
    RecyclerView.Adapter<WatchFaceAdapter.RvViewHolder>() {
    var list = emptyList<WatchFaceModel>()
    var sholdShowLoader = true
    lateinit var wfContract: WfContract

    inner class RvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivWatchFace: ImageView = itemView.findViewById(R.id.ivWatchFace)
        var tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
        var tvDownloads: TextView = itemView.findViewById(R.id.tvDownloads)
        var ivDownload: ImageView = itemView.findViewById(R.id.ivDownload)
        var pbDownload: ProgressBar = itemView.findViewById(R.id.pbDownload)


        fun bindData(watchFaceModel: WatchFaceModel) {
            Picasso.with(context).load(watchFaceModel.img_url).centerCrop().fit().into(ivWatchFace)
            /*itemView.setOnClickListener {
                wfContract.requestPermission()
            }*/
            tvLanguage.text = watchFaceModel.language
            tvDownloads.text = watchFaceModel.downloads
            if (!sholdShowLoader) {
                ivDownload.visibility = View.VISIBLE
                pbDownload.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.watch_face_item, parent, false)
        return RvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    fun setContract(wfContract: WfContract) {
        this.wfContract = wfContract
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        holder.bindData(list.get(position))
        holder.ivDownload.setOnClickListener {
            holder.ivDownload.visibility = View.GONE
            holder.pbDownload.visibility = View.VISIBLE

            val downloadmanager =
                context.applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
            val uri =
                Uri.parse(list.get(position).wf_url)

            val request = DownloadManager.Request(uri)
            request.setTitle("miWatchFace" + position)
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalFilesDir(
                context,
                "wf",
                "2101"
            )

//file:///storage/emulated/0/Android/data/com.ankit.miwatchface/files/wf/2101
        //    request.setDestinationUri(Uri.parse("file:///storage/emulated/0/Android/data/com.xiomi.hm.health/files/watch_skin_file/59"))

            downloadmanager!!.enqueue(request)

        }
    }

    fun copyFile() {


        val sourcePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        val source = File(sourcePath, "6317.gif")
       // source.mkdir()
        val destinationPath =
            "file:///storage/emulated/0/Android/data/com.xiomi.hm.health/files/watch_skin_file/59"
        val destination = File(destinationPath)
        try {
            FileUtils.copyFile(source, destination)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun updateList(list: List<WatchFaceModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun stopLoading() {
        copyFile()
        sholdShowLoader = false
        notifyDataSetChanged()
    }
}