package com.example.marvelapp.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.marvelapp.databinding.ErrorDialogFragmentBinding
import com.example.marvelapp.model.error.BaseError

class ErrorDialogFragment : DialogFragment() {

    private var _binding: ErrorDialogFragmentBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = ErrorDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.errorButton.setOnClickListener {
            listener?.onButtonClick()
            dismiss()
        }

        errorResponse?.let { error ->
            binding.errorMessage.text = error.errorMessageString
            binding.errorCode.text = error.errorCode?.name
        }
    }

    interface ErrorListener {
        fun onButtonClick()
    }
}
