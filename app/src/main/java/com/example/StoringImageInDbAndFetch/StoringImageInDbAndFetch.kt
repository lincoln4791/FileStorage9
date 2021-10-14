package com.example.StoringImageInDbAndFetch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.filestorageandroid9.R
import com.example.filestorageandroid9.databinding.ActivityStoringImageInDbAndFetchBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class StoringImageInDbAndFetch : AppCompatActivity() {

    lateinit var binding : ActivityStoringImageInDbAndFetchBinding
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
            saveFromAssets()
        }

        binding.btnLoadAsset.setOnClickListener {
            loadFromAssets()
        }


    }




    private fun saveImageInDB() {
        var outFileName = "myImage.png"
        var outFile = File(getExternalFilesDir("MyImageFolder"),outFileName)
        val inputBitmap  = BitmapFactory.decodeResource(this@StoringImageInDbAndFetch.resources,R.drawable.dating_app)

        val fos = FileOutputStream(outFile)
        inputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        Toast.makeText(this@StoringImageInDbAndFetch,"Ssve Done",Toast.LENGTH_LONG).show()
        Log.d("tag","save Done")
        fos.close()
    }




    private fun loadImage() {
        var inFileName = "myImage.png"
        var inFile = File(getExternalFilesDir("MyImageFolder"),inFileName)
        var bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
        binding.iv.setImageBitmap(bitmap)

    }


    private fun saveFromAssets() {
        var outFileName = "myImageFromAssets.jpg"
        var outFile = File(getExternalFilesDir("MyImageFolder"),outFileName)
        var inputStream = assets.open("ads/cycling.jpg")
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val fos = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.close()
        Toast.makeText(this@StoringImageInDbAndFetch,"Ssve Done",Toast.LENGTH_LONG).show()
        Log.d("tag","save Done")

    }


    private fun loadFromAssets(){
        var inFileName = "myImageFromAssets.jpg"
        var inFile = File(getExternalFilesDir("MyImageFolder"),inFileName)
        var bitmap = BitmapFactory.decodeStream(FileInputStream(inFile))
        binding.iv.setImageBitmap(bitmap)
    }



}