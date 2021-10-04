package com.example.marvelapp.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.marvelapp.R
import com.example.marvelapp.model.error.BaseError
import kotlinx.android.synthetic.main.error_dialog_fragment.*

class ErrorDialogFragment : DialogFragment() {

    private var listener: ErrorListener? = null
    private var errorResponse: BaseError? = null

    companion object {
        const val ERROR_FRAGMENT_TAG = "ErrorDialogFragment"

        fun newInstance(error: BaseError, listener: ErrorListener?): ErrorDialogFragment {
            return ErrorDialogFragment().apply {
                this.errorResponse = error
                this.listener = listener
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.error_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorButton.setOnClickListener {
            listener?.onButtonClick()
            dismiss()
        }

        errorResponse?.let { error ->
            errorMessage.text = error.errorMessage
            errorCode.text = error.errorCode?.name
        }
    }


    interface ErrorListener {
        fun onButtonClick()
    }
}