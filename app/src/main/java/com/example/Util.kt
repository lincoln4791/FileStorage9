package com.example

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.example.worker.SaveMissingBannerImages
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL

class Util {
    companion object{
        fun syncPendingOnlineBookmark(applicationContext: Context){
            CoroutineScope(Dispatchers.Default).launch {
                Log.d("tag","Pending Sync Launched")
                Paper.init(applicationContext)
                try {
                    val missingImageList : MutableList<MissingImageModelClass> = Paper.book().read(Constants.MISSING_IMAGE_BANNER_LIST)
                    Log.d("tag","List Size is ${missingImageList.size}")
                    if(missingImageList.size>0){
                        for(element in missingImageList){
                            saveMissingImage(applicationContext,element.fileName,element.imgUrlString)
                            Log.d("tag","trying to save element in missing list")
                        }
                    }
                    else{
                        Log.d("tag","list is empty")
                    }

                }
                catch (e : Exception) {
                    Log.d("tag","Exception in Pending Sync Bookmark ${e.message} and ${e.localizedMessage}")
                    e.printStackTrace()
                }
            }

        }


        fun saveMissingImage(context: Context, fileName:String, imgUrlString : String) {

            Log.d("tag","save missing image util called fileName -> $fileName , mediaUrl -> $imgUrlString")

            val myData: Data = workDataOf(Constants.MISSING_BANNER_FILE_NAME to fileName
                ,Constants.MISSING_BANNER_FILE_URL to imgUrlString)

            val workRequest: WorkRequest =
                OneTimeWorkRequestBuilder<SaveMissingBannerImages>().setInputData(myData)
                    .build()

            val saveWorker = WorkManager
                .getInstance(context)
            saveWorker.enqueue(workRequest)

        }


        fun addInMissingImgFileList(context: Context,fileName:String,url: String){
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("tag","adding in missing list")
                Paper.init(context)
                val missingList : MutableList<MissingImageModelClass> = Paper.book().read(Constants.MISSING_IMAGE_BANNER_LIST)
                val element = MissingImageModelClass(fileName,url)
                missingList.add(element)

                Paper.book().delete(Constants.MISSING_IMAGE_BANNER_LIST)
                Paper.book().write(Constants.MISSING_IMAGE_BANNER_LIST,missingList)
                Log.d("tag","added in missing list")
            }
        }


    }
}