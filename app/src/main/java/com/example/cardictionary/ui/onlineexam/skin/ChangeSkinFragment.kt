package com.example.cardictionary.ui.onlineexam.skin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cardictionary.MainViewModel
import com.example.cardictionary.databinding.FragmentChangeSkinBinding
import com.example.cardictionary.tool.saveThemeId
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeSkinFragment : BottomSheetDialogFragment() {
    private val mainViewModel = MainViewModel.getInstance()
    private lateinit var binding: FragmentChangeSkinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeSkinBinding.inflate(layoutInflater, container, false)

        val id = mainViewModel.getThemeId()
        if (id == 1) {
            binding.theme1Selected.visibility = View.VISIBLE
            binding.theme2Selected.visibility = View.INVISIBLE
        } else {
            binding.theme1Selected.visibility = View.INVISIBLE
            binding.theme2Selected.visibility = View.VISIBLE
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.theme1.setOnClickListener {
            binding.theme1Selected.visibility = View.VISIBLE
            binding.theme2Selected.visibility = View.INVISIBLE
            saveThemeId(requireContext(), 1)
            lifecycleScope.launch(Dispatchers.IO) {
                delay(500)
                withContext(Dispatchers.Main) {
                    requireActivity().recreate()
                    findNavController().navigateUp()
                }
            }
        }

        binding.theme2.setOnClickListener {
            binding.theme1Selected.visibility = View.INVISIBLE
            binding.theme2Selected.visibility = View.VISIBLE
            saveThemeId(requireContext(), 2)
            lifecycleScope.launch(Dispatchers.IO) {
                delay(500)
                withContext(Dispatchers.Main) {
                    requireActivity().recreate()
                    findNavController().navigateUp()
                }
            }
        }
    }
}