package com.example.StoringImageInDbAndFetch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.filestorageandroid9.R
import com.example.filestorageandroid9.databinding.ActivityStoringImageInDbAndFetchBinding
import androidx.work.*
import com.example.Constants
import com.example.MissingImageModelClass
import com.example.Util
import io.paperdb.Paper
import kotlinx.coroutines.*
import java.io.*
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import android.os.Environment





class StoringImageInDbAndFetch : AppCompatActivity() {

    lateinit var binding: ActivityStoringImageInDbAndFetchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoringImageInDbAndFetchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkMissingFileNullability()
        //downloadMissingImage()


        binding.btnSaveImage.setOnClickListener {
            saveImageInDB()
        }

        binding.btnLoadImage.setOnClickListener {
            loadImage()
        }

        binding.btnSaveFromAsset.setOnClickListener {
            saveImageFromResources()
        }

        binding.btnLoadAsset.setOnClickListener {
            loadFromAssets()
        }

        binding.retry.setOnClickListener {
            downloadMissingImage()
        }

        binding.btnEmpty.setOnClickListener {
            var isMyFileExists = false
            val fileName = "gg.png"
            val folderName = "MyEmptyFolder"
            //var myFile = File("${getExternalFilesDir("$folderName/$fileName")}")
            val filesOfTheFolder : ArrayList<String> =  getFileNameOfFolder(folderName)
            Log.d("tag",filesOfTheFolder.toString())

            for(i in filesOfTheFolder.indices){
                if(filesOfTheFolder[i]== fileName){
                    Log.d("tag"," File Already exists")
                    isMyFileExists = true
                    break
                }
            }

            if(!isMyFileExists){
                Log.d("tag"," File Not Exists")
                val file = File("${getExternalFilesDir("$folderName/$fileName")}")
            }

        }
    }

    private fun getFileNameOfFolder(folderName:String): java.util.ArrayList<String> {
        val fileNamesList : ArrayList<String> = ArrayList<String>()
        val path = getExternalFilesDir(folderName).toString()
        Log.d("tag", "Path: $path")
        val f = File(path)
        val file = f.listFiles()
        Log.d("tag", "Size: " + file.size)
        for (i in file.indices) {
            //here populate your listview
            fileNamesList.add(file[i].name)
            //Log.d("tag", "FileName:" + file[i].name)
        }
        return fileNamesList
    }

    private fun downloadMissingImage() {
        Util.syncPendingOnlineBookmark(applicationContext)
    }

    private fun checkMissingFileNullability() {
        CoroutineScope(Dispatchers.IO).launch {
            Paper.init(applicationContext)
         /*   Paper.book().delete(Constants.MISSING_IMAGE_BANNER_LIST)
            val emptyList : MutableList<MissingImageModelClass> = mutableListOf()
            Paper.book().write(Constants.MISSING_IMAGE_BANNER_LIST,emptyList)*/
            val missingImageList: MutableList<MissingImageModelClass>? =
                Paper.book().read(Constants.MISSING_IMAGE_BANNER_LIST)
            if (missingImageList == null) {
                val emptyMissingImageList: MutableList<MissingImageModelClass> = mutableListOf()
                Paper.book().write(Constants.MISSING_IMAGE_BANNER_LIST, emptyMissingImageList)
            }
        }
    }


    private fun saveImageInDB() {
        saveImageFromUrl()
    }


    private fun loadImage() {
        val inFileName = "myImage.png"
        val inFile = File(getExternalFilesDir("MyImageFolder"), inFileName)
        var bitmap: Bitmap? = null

        try {
            bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "File Not Found", Toast.LENGTH_SHORT).show()
        }

        if (bitmap != null) {
            try {
                binding.iv.setImageBitmap(bitmap)
                Toast.makeText(this@StoringImageInDbAndFetch, " File Loaded", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(this@StoringImageInDbAndFetch, "Bad Image", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Bitmap is null", Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveImageFromResources() {
        val outFileName = "myImage.png"
        val outFile = File(getExternalFilesDir("MyImageFolder"), outFileName)
        val inputBitmap = BitmapFactory.decodeResource(this@StoringImageInDbAndFetch.resources,
            R.drawable.dating_app)

        val fos = FileOutputStream(outFile)
        inputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
        Log.d("tag", "save Done")
        fos.close()
    }


    private fun loadFromAssets() {

        try {
            val inFileName = "myImageFromAssets.jpg"
            val inFile = File(getExternalFilesDir("MyImageFolder"), inFileName)
            val bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
            binding.iv.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(this@StoringImageInDbAndFetch, "Image Not Saved Yet", Toast.LENGTH_LONG)
                .show()
        }
    }


    private fun saveImageFromUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            val outFileName = "myImage.png"
            val outFile = File(getExternalFilesDir("MyImageFolder"), outFileName)
            val linkStr = "http://157.245.156.73/ads/Find_doctor_bn.jpg"
            val imgUrl = URL(linkStr)
            try {

                delay(400)
                val bitmap =
                    BitmapFactory.decodeStream(
                        imgUrl.openStream())
                val fos = FileOutputStream(outFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                Log.d("tag", "save Done")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Save Done", Toast.LENGTH_SHORT).show()
                }
                //Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.d("tag", "Save Failed")
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(applicationContext, "Save Faled", Toast.LENGTH_SHORT).show()
                }

                Util.addInMissingImgFileList(applicationContext,outFileName, linkStr)

            }
        }


        fun saveImageFromAsset() {
            val outFileName = "myImageFromAssets.jpg"
            val outFile = File(getExternalFilesDir("MyImageFolder"), outFileName)
            val inputStream = assets.open("ads/cycling.jpg")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val fos = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            Toast.makeText(this@StoringImageInDbAndFetch, "Save Done", Toast.LENGTH_LONG).show()
            Log.d("tag", "save Done")
        }

    }
}