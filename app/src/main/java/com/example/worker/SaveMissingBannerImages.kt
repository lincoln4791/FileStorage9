package com.example.worker

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class SaveMissingBannerImages(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG,"Thread of do work method is ${Thread.currentThread().name}")

        return Result.success()
    }


    companion object {
        const val TAG = "SaveMissingBannerImage"
    }
}