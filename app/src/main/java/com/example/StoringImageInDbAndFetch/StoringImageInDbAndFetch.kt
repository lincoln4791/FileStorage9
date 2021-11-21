package com.example.StoringImageInDbAndFetch

import android.R.attr
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.filestorageandroid9.R
import com.example.filestorageandroid9.databinding.ActivityStoringImageInDbAndFetchBinding
import android.R.attr.bitmap
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.example.Constants
import com.example.worker.SaveMissingBannerImages
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.net.URL
import kotlin.coroutines.CoroutineContext









class StoringImageInDbAndFetch : AppCompatActivity() {

    lateinit var binding: ActivityStoringImageInDbAndFetchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoringImageInDbAndFetchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSaveImage.setOnClickListener {
            saveImageInDB()
        }

        binding.btnLoadImage.setOnClickListener {
            loadImage()
        }

        binding.btnSaveFromAsset.setOnClickListener {
            saveImageFromAsset()
        }

        binding.btnLoadAsset.setOnClickListener {
            loadFromAssets()
        }
    }


    private fun saveImageInDB() {
        saveImageFromUrl()
    }


    private fun loadImage() {
        val inFileName = "myImage.png"
        val inFile = File(getExternalFilesDir("MyImageFolder"), inFileName)
        val bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))

        try {
            binding.iv.setImageBitmap(bitmap)
            Toast.makeText(this@StoringImageInDbAndFetch, " File Loaded",Toast.LENGTH_SHORT).show()
        }
        catch (e:Exception){
            Toast.makeText(this@StoringImageInDbAndFetch, "Bad Image",Toast.LENGTH_SHORT).show()
        }



    }

    private fun saveImageFromResources(){
        val outFileName = "myImage.png"
        val outFile = File(getExternalFilesDir("MyImageFolder"),outFileName)
        val inputBitmap  = BitmapFactory.decodeResource(this@StoringImageInDbAndFetch.resources,R.drawable.dating_app)

        val fos = FileOutputStream(outFile)
        inputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        Toast.makeText(this@StoringImageInDbAndFetch,"Ssve Done",Toast.LENGTH_LONG).show()
        Log.d("tag","save Done")
        fos.close()
    }



    private fun loadFromAssets() {

        try {
            val inFileName = "myImageFromAssets.jpg"
            val inFile = File(getExternalFilesDir("MyImageFolder"), inFileName)
            val bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
            binding.iv.setImageBitmap(bitmap)
        }
        catch (e:Exception){
            Toast.makeText(this@StoringImageInDbAndFetch,"Image Not Saved Yet",Toast.LENGTH_LONG).show()
        }
    }


    fun saveImageFromUrl(){
        CoroutineScope(Dispatchers.IO).launch {
            val outFileName = "myImage.png"
            val outFile = File(getExternalFilesDir("MyImageFolder"), outFileName)
            try{

                val stringUrl = URL("http://157.245.156.73/ads/Find_doctor_bn.jpg")
                delay(400)
                val bitmap =
                    BitmapFactory.decodeStream(
                        stringUrl.openStream())
                val fos = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                Log.d("tag", "save Done")
                //Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
            }
            catch (e:Exception){
                Log.d("tag","Save Failed")
                if(outFile.exists()){
                    outFile.delete()
                    Log.d("tag","File Deleted")
                }

            }

        }
    }


    private fun saveImageFromAsset(){
        val outFileName = "myImageFromAssets.jpg"
        val outFile = File(getExternalFilesDir("MyImageFolder"), outFileName)
        val inputStream = assets.open("ads/cycling.jpg")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val fos = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
        Log.d("tag", "save Done")
    }

    fun saveBookmarkInBothLocalAndServer(context: Context, lifeCycleOwner: LifecycleOwner, fileName: String,mediaUrl:String, itemType: String) {

        Log.d("Bookmark","bookmark util called fileName -> $fileName , mediaUrl -> $mediaUrl")

        val myData: Data = workDataOf(Constants.MISSING_BANNER_FILE_NAME to fileName,
            Constants.MISSING_BANNER_FILE_URL to mediaUrl)

        val saveBookmarkWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<SaveMissingBannerImages>().setInputData(myData)
                .build()

        val saveWorker = WorkManager
            .getInstance(context)
        saveWorker.enqueue(saveBookmarkWorkRequest)

        /*    saveWorker.getWorkInfoByIdLiveData(saveBookmarkWorkRequest.id).observe(lifeCycleOwner,{
                if (it.state.isFinished){
                    Log.d(DrugCost.TAG,"SAVE BOOKMARK DONE")
                }
            })*/

    }


}