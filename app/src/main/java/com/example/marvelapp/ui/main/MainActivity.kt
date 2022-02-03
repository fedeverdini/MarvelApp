package com.example.marvelapp.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import com.example.marvelapp.R
import com.example.marvelapp.databinding.MainActivityBinding
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.list.CharacterListFragment
import com.example.marvelapp.ui.main.ErrorDialogFragment.Companion.ERROR_FRAGMENT_TAG
import com.example.marvelapp.ui.main.LoadingDialogFragment.Companion.LOADING_FRAGMENT_TAG

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private var isLoading = false
    private val loadingDialog = LoadingDialogFragment()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.mainFragmentContainer)
        navController.navigatorProvider.addNavigator(
            DialogFragmentNavigator(this, supportFragmentManager)
        )
        if (savedInstanceState == null) {
            navController.navigate(R.id.characterListFragment)
        }
    }

    fun showLoading(value: Boolean) {
        supportFragmentManager.let { fragmentManager ->
            when {
                !isLoading && value -> {
                    loadingDialog.show(fragmentManager, LOADING_FRAGMENT_TAG)
                }
                isLoading && !value -> {
                    loadingDialog.dismiss()
                }
                else -> Unit
            }
            isLoading = value
        }
    }

    fun showError(error: BaseError, listener: ErrorDialogFragment.ErrorListener? = null) {
        val dialog = ErrorDialogFragment.newInstance(error, listener)
        dialog.show(supportFragmentManager, ERROR_FRAGMENT_TAG)
    }
}
