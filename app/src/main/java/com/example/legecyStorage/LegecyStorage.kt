package com.example.legecyStorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.filestorageandroid9.R
import com.example.filestorageandroid9.databinding.ActivityLegecyStorageBinding
import com.example.filestorageandroid9.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.*
import java.lang.StringBuilder
import java.util.jar.Manifest

class LegecyStorage : AppCompatActivity() {

    lateinit var binding: ActivityLegecyStorageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegecyStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnWrite.setOnClickListener {
            Dexter.withContext(this@LegecyStorage).withPermission(android.Manifest.permission
                .WRITE_EXTERNAL_STORAGE).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Log.d("tag", "Permission Granted")
                    writeFile()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Log.d("tag", "Permission Denied")
                    Toast.makeText(this@LegecyStorage, "Permission Needed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    Log.d("tag", "Relational")
                    p1!!.continuePermissionRequest()
                }

            }).onSameThread().check()


        }

        binding.btnRead.setOnClickListener {
            readLine()
        }


    }


    fun writeFile() {
        var fileName: String = "demoFile.txt"
        var folderName =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var fos: FileOutputStream? = null
        var dataToWrite = "1"
        var file: File = File(folderName, fileName)


        fos = FileOutputStream(file)
        fos.write(dataToWrite.toByteArray())
        fos.close()
        Log.d("tag", "File Saved to " + filesDir.canWrite() + "/" + fileName)
    }


    fun readFile() {
        var fileName: String = "demoFile.txt"
        var openFileInput = openFileInput(fileName)
        var isr = InputStreamReader(openFileInput)
        var bufferReader: BufferedReader = BufferedReader(isr)
        var lines: List<String> = bufferReader.readLines()
        var stringBuilder: StringBuilder = StringBuilder()

        for (line in lines) {
            stringBuilder.append(line + "\n")
        }

        Log.d("tag", " Lines Are $stringBuilder")
    }

}