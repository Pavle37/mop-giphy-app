package com.nebulis.mopgiphyapp.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nebulis.mopgiphyapp.R
import kotlinx.android.synthetic.main.gif_fullscreen.*


class GifFullscreenFragment : Fragment(){

    private val args: GifFullscreenFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(requireContext()).load(args.gifUrl).into(ivFullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gif_fullscreen, container, false)
    }
}