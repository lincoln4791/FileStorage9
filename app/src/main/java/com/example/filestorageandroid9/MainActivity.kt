package com.example.filestorageandroid9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.filestorageandroid9.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {


    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWrite.setOnClickListener {



            var fileName : String = "myDemoFil.txt"
            var folderName :String = "MyDemoFolder"
            var dataToWrite :String = binding.et1.text.toString()
            var myDemoFile : File= File(getExternalFilesDir(folderName),fileName)
            var fos : FileOutputStream? = null
            Log.d("tag","Opend")

            try {
                Log.d("tag","Started")
                fos = FileOutputStream(myDemoFile)
                fos.write(dataToWrite.toByteArray())
                fos.close()
                Log.d("tag","Success")
                Toast.makeText(this@MainActivity,"Write Success",Toast.LENGTH_LONG).show()
            }
            catch (e:IOException){
                Log.d("tag","Failed To Write")
            }
        }

    }
}