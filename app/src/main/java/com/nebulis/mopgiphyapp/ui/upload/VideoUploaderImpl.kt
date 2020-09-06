package com.nebulis.mopgiphyapp.ui.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.data.network.client.GiphyUploadRestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 100mb to bytes
 */
private const val BYTE_SIZE_LIMIT = 100000000

class VideoUploaderImpl(private val appContext: Context, private val uploadClient: GiphyUploadRestClient): VideoUploader{

    private val contentResolver  = appContext.contentResolver

    override fun uploadVideoFromUri(videoUri: Uri) {
        contentResolver.query(videoUri, null, null, null, null).let { cursor ->
            cursor ?: return
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()

            val size = cursor.getLong(sizeIndex)
            val name = cursor.getString(nameIndex)
            val type = contentResolver.getType(videoUri)

            /*Limit file upload to 100mb*/
            if (size > BYTE_SIZE_LIMIT) {
                showToast(R.string.file_too_big)
                return
            }
            /*Inform user about the upload taking place*/
            showToast(R.string.uploading)

            /*Upload*/
            initiateUploadRequest(videoUri,name,type)
            }
        }

    /**
     * Initiates an uploader request. Uses global scope so that the upload won't cancel if there is
     * a change in the activity state. Informs the user about status or error.
     *
     * @param videoUri - uri representing the selected video to upload.
     * @param name - name of the file that's being uploaded.
     * @param type - MimeType of the file that's being uploaded.
     *
     */
    private fun initiateUploadRequest(videoUri: Uri, name: String, type: String?) = GlobalScope.launch(Dispatchers.IO){
            try {
                val response = uploadClient.uploadImage(appContext, videoUri, name, type ?: GENERIC_VIDEO_TYPE)
                val status = response.metaData
                showToast("${status.status}, ${status.msg}")
            } catch (e: Exception) {
               showToast(R.string.failed_to_upload)
        }
    }


    /**
     * Shows toast.
     *
     * @param msg - string that's going to be toasted.
     * @param length - time that toast will be shown.
     */
    private fun showToast(msg: String, length: Int = Toast.LENGTH_SHORT){
        GlobalScope.launch(Dispatchers.Main) {//Toast can be called from bg thread, so we need to switch
            Toast.makeText(appContext, msg, length).show()
        }
    }

    /**
     * Shows toast.
     *
     * @param msg - string resource id.
     * @param length - time that toast will be shown.
     */
    private fun showToast(msg: Int, length: Int = Toast.LENGTH_SHORT){
        showToast(appContext.getString(msg),length)
    }

}
