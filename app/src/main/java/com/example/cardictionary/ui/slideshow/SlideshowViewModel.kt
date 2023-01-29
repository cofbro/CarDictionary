package com.example.cardictionary.ui.slideshow


import android.content.Context
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.cardictionary.R
import com.example.cardictionary.databinding.FragmentSlideshowBinding
import com.example.cardictionary.tool.User

class SlideshowViewModel : ViewModel() {
    fun loadUserInfo(
        context: Context,
        binding: FragmentSlideshowBinding,
        currentUser: User?
    ) {
        if (currentUser != null) {
            if (currentUser.username != "" && currentUser.userAvatar != "") {
                binding.name.text = currentUser.username
                Glide.with(context)
                    .load(currentUser.userAvatar)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.avatar)
            }
        } else {
            binding.name.text = "未登录"
            binding.avatar.setImageResource(R.mipmap.ic_launcher)
        }
    }
}