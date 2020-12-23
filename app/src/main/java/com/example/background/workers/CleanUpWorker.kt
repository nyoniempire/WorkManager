package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import timber.log.Timber
import java.io.File
import java.lang.reflect.Parameter

class CleanUpWorker(ctx: Context, params: WorkerParameters): Worker(ctx,params) {

    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temp files",applicationContext)
        sleep()

        return try{
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if(outputDirectory.exists()){
                val entries = outputDirectory.listFiles()
                if(entries!=null){
                    for(entry in entries){
                        val name = entry.name
                        if(name.isNotEmpty() && name.endsWith(".png")){
                            val deleted = entry.delete()
                            Timber.i("Output deleted: $deleted")
                        }
                    }
                }
            }
            Result.success()
        }catch (ex: Throwable){

            Result.failure()
        }
    }
}