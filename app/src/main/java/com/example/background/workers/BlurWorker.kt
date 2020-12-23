package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import java.lang.IllegalArgumentException

class BlurWorker(ctx: Context, wrkParams: WorkerParameters): Worker(ctx, wrkParams) {


    override fun doWork(): Result {
        val appContext: Context = applicationContext
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring Image",appContext)

        sleep()

        return try {
            val resolver = appContext.contentResolver

            if(TextUtils.isEmpty(resourceUri)) throw IllegalArgumentException("Invalid Input")

            val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
            )

            val output = blurBitmap(picture,appContext)
            val outputUri = writeBitmapToFile(appContext,output)

            makeStatusNotification("Output is $outputUri",appContext)

            val workData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(workData)
        }catch (throwable: Throwable){
            makeStatusNotification("Blurring failed",appContext)
            Result.failure()
        }

    }
}