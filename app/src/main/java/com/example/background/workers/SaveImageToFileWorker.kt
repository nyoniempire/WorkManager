package com.example.background.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.CaseMap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import java.util.*

class SaveImageToFileWorker(ctx: Context,wrkrParams: WorkerParameters): Worker(ctx,wrkrParams) {

    private val Title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
            "yyyy.MM.dd 'at' HH:mm:ss z",
            Locale.getDefault()
    )

    override fun doWork(): Result {
        makeStatusNotification("Saving Image",applicationContext)

        sleep()

        val resolver = applicationContext.contentResolver
        return try {

            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
            )

            val imageUri = MediaStore.Images.Media.insertImage(
                    resolver,bitmap,Title,dateFormatter.format(Date())
            )

            if(!imageUri.isNullOrEmpty()){
                val output = workDataOf(KEY_IMAGE_URI to imageUri)
                Result.success(output)
            }else{
                Result.failure()
            }
        }catch (ex: Throwable){
            Result.failure()
        }
    }
}