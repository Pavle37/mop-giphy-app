package com.nebulis.mopgiphyapp.util

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.Charset


fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

fun <T> MutableLiveData<T>.forceRefresh() {
    this.postValue(this.value)
}

/**
 * Utility class with functions that return RequestBody objects from classes
 */
class RequestBodyParser {
    companion object {
        /**
         * Returns RequestBody object from parsed Uri
         */
        fun parseUri(
            context: Context,
            fieldName: String,
            uri: Uri,
            fileName: String,
            type: String
        ): MultipartBody.Part {
            val mediaType = type.toMediaTypeOrNull()!!
            mediaType.charset(Charset.forName("utf-8"))

            val stream = context.contentResolver.openInputStream(uri)

            val byteArray = stream?.readBytes()!!
            stream.close()

            val fileRequest = byteArray.toRequestBody()
            return MultipartBody.Part.createFormData(fieldName, fileName, fileRequest)
        }

        /**
         * Returns RequestBody object from parsed String
         */
        fun parseString(stringToBeParsed: String?): RequestBody? {
            if (stringToBeParsed == null) return null
            val mediaType = "text/plain".toMediaTypeOrNull()!!
            mediaType.charset(Charset.forName("utf-8"))
            return stringToBeParsed.toRequestBody(mediaType)
        }
    }
}