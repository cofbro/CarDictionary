package com.example.cardictionary.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cardictionary.R
import com.example.cardictionary.databinding.FragmentLoginBinding
import com.example.cardictionary.tool.BaseUiListener
import com.tencent.tauth.Tencent


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var mTencent: Tencent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mTencent = Tencent.createInstance("102036486", requireContext())
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registryFragment)
        }

        binding.button.setOnClickListener {
            if (binding.userNameView.text.isEmpty() || binding.passwordEditText.text.isEmpty()) {
                loginViewModel.loginNormalAccount(requireContext(), binding)
            } else {
                Toast.makeText(requireContext(), "您还没有输入完整呢~", Toast.LENGTH_SHORT).show()
            }
        }

        binding.qqLogin.setOnClickListener {
            loginViewModel.loginWithQQAccount(requireActivity(), mTencent)
        }
    }

}