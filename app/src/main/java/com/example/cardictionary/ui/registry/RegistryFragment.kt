package com.example.cardictionary.ui.registry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.databinding.FragmentRegistryBinding

class RegistryFragment : Fragment() {
    private lateinit var binding: FragmentRegistryBinding
    private val registryViewModel: RegistryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerButton.setOnClickListener {
            registryViewModel.registryAccount(requireContext(), binding)
        }
    }
}