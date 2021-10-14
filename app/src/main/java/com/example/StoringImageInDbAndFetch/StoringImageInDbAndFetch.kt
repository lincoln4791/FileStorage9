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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        binding.iv.setImageBitmap(bitmap)

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
            val stringUrl = URL("http://157.245.156.73/ads/Find_doctor_bn.jpg")
            val bitmap =
                BitmapFactory.decodeStream(
                    stringUrl.openStream())
            val fos = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            Log.d("tag", "save Done")
            //Toast.makeText(this@StoringImageInDbAndFetch, "Ssve Done", Toast.LENGTH_LONG).show()
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

}