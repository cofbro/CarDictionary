package com.example.cardictionary.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.R
import com.example.cardictionary.databinding.FragmentSlideshowBinding
import com.example.cardictionary.tool.saveThemeId
import com.example.skin.SkinManager

class SlideshowFragment : Fragment() {
    private val slideshowViewModel: SlideshowViewModel by viewModels()
    private var _binding: FragmentSlideshowBinding? = null
    private val mainViewModel = MainViewModel.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mainViewModel.isLogin.observe(viewLifecycleOwner) {
            val currentUser = mainViewModel.getCurrentUser()
            slideshowViewModel.loadUserInfo(requireContext(), binding, currentUser)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.avatar.setOnClickListener {
            if (mainViewModel.getCurrentUser() == null) {
                findNavController().navigate(R.id.action_nav_slideshow_to_loginFragment)
            }
        }

        binding.account.setOnClickListener {
            if (mainViewModel.getCurrentUser() == null) {
                findNavController().navigate(R.id.action_nav_slideshow_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "您已经登录过了哦~", Toast.LENGTH_SHORT).show()
            }
        }

        binding.themeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_slideshow_to_changeSkinFragment)
        }

        binding.quitAccount.setOnClickListener {
            if (mainViewModel.getCurrentUser() != null) {
                mainViewModel.clearCurrentUser()
                if (mainViewModel.mTencent != null) {
                    mainViewModel.mTencent!!.logout(requireContext())
                }
            } else {
                Toast.makeText(requireContext(), "您还未登录哦~", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}