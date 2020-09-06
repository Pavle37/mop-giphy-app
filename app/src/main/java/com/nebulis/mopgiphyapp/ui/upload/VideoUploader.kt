package com.nebulis.mopgiphyapp.ui.upload

import android.net.Uri

const val GENERIC_VIDEO_TYPE = "video/*"

interface VideoUploader {
    /**
     * Handles uploading video to giphy service.
     *
     * @param videoUri - content uri for the selected video.
     */
    fun uploadVideoFromUri(videoUri: Uri)
}