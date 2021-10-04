package com.example.marvelapp.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.marvelapp.R
import kotlinx.android.synthetic.main.loading_dialog_fragment.*
import timber.log.Timber
import java.lang.Exception

class LoadingDialogFragment: DialogFragment() {
    companion object {
        const val LOADING_FRAGMENT_TAG = "LoadingDialogFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.let {
            val paramSize = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window?.setLayout(paramSize, paramSize)
        }

        super.onViewCreated(view, savedInstanceState)
        try {
            loadingAnimation.setAnimation("loading-spinner.json")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}