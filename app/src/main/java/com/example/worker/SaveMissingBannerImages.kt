package com.example.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.Constants
import com.example.MissingImageModelClass
import com.example.Util
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL


class SaveMissingBannerImages(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("tag","Thread of do work method is ${Thread.currentThread().name}")
        CoroutineScope(Dispatchers.IO).launch {
            val outFileName = "myImage.png"
            val outFile = File(applicationContext.getExternalFilesDir("MyImageFolder"), outFileName)
             val linkStr = "http://157.245.156.73/ads/Find_doctor_bn.jpg"
            val imgUrl = URL(linkStr)
            try {
                //delay(400)
                val bitmap =
                    BitmapFactory.decodeStream(
                        imgUrl.openStream())
                val fos = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                Log.d("tag", "save Done from worker")

                deleteFromMissingImgList(outFileName)

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Save Done from worker", Toast.LENGTH_SHORT).show()
                }
                //Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.d("tag", "Save Failed from worker")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Save Faled from worker", Toast.LENGTH_SHORT).show()
                }

                Log.d("tag", "File Deleted")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Save Failed frommworker", Toast.LENGTH_SHORT).show()
                }


            }
        }
        return Result.success()
    }

    private fun deleteFromMissingImgList(fileName: String){
        CoroutineScope(Dispatchers.IO).launch {
            Paper.init(applicationContext)
            val missingList : MutableList<MissingImageModelClass> = Paper.book().read(Constants.MISSING_IMAGE_BANNER_LIST)
            var deleteIndex = -1

            for(i in missingList.indices){
                if(missingList[i].fileName == fileName){
                    deleteIndex = i
                }
            }

            if(deleteIndex!= -1){
                missingList.removeAt(deleteIndex)
            }

            Paper.book().delete(Constants.MISSING_IMAGE_BANNER_LIST)
            Paper.book().write(Constants.MISSING_IMAGE_BANNER_LIST,missingList)
            Log.d("tag","deleted from missing list")

        }
    }


    companion object {
        const val TAG = "SaveMissingBannerImage"
    }
}