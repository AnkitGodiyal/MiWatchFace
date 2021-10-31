package com.ankit.miwatchface.Model

data class WatchFaceListModel(val data:List<WatchFaceModel>){}

data class WatchFaceModel(
    val img_url:String="",
    val wf_url:String="",
    val downloads:String="",
    val language:String="",
    val info_url:String=""
) {
}

