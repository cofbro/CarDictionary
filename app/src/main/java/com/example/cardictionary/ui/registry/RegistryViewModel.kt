package com.example.cardictionary.ui.registry

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.cardictionary.databinding.FragmentRegistryBinding
import com.example.cardictionary.tool.LCUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistryViewModel(app: Application): AndroidViewModel(app) {
    private val lcUtils = LCUtils()

    fun registryAccount(context: Context, binding: FragmentRegistryBinding) {
        val username = binding.userNameTextView.text.toString()
        val password = binding.psdTextView.text.toString()
        val email = binding.emailTextView.text.toString()
        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                lcUtils.registerUserUsedLC(context, username, password, email)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "请前往邮箱确认",Toast.LENGTH_LONG).show()
                    binding.root.findNavController().navigateUp()
                }
            }
        } else {
            Snackbar.make(binding.psdTextView, "输入框不能为空", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}